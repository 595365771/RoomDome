package com.blackcat.roomdome;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import io.reactivex.schedulers.Schedulers;

public class ProductBrowsingHistoryHelper {

    /**
     * 倒序分页查询，通过字符串集合对pID进行去重，然后拼接提交给后台
     *
     * @param context     context
     * @param mDisposable mDisposable
     * @param pageNumber  页码
     * @param oldStr      已经拼接完的商品ID
     */
    public static void queryProductLimit(Application context, CompositeDisposable mDisposable, int pageNumber, String oldStr,DatabaseCallBack<String> callBack) {
        ProductBrowsingHistoryDao productBrowsingHistoryDao = AppDatabase.getInstance(context).productBrowsingHistoryDao();
        mDisposable.add(productBrowsingHistoryDao.queryProductLimit(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(productBrowsingHistories -> {
                    StringBuffer buffer = new StringBuffer();
                    for (ProductBrowsingHistory productBrowsingHistorie : productBrowsingHistories) {
                        if (!oldStr.contains(productBrowsingHistorie.productID)) {
                            buffer.append(productBrowsingHistorie.productID).append(",");
                        }
                    }
                    int index = buffer.lastIndexOf(",");
                    if (index > 0) {
                        buffer.deleteCharAt(index);
                    }
                    callBack.onSuccess(buffer.toString());
                }, throwable -> {
                    Log.e("-----------查询失败", ",查询失败");
                    callBack.onError(throwable);
                }));
    }

    public static String deleteAllProductLimit(Application context, CompositeDisposable mDisposable, int pageNumber, String oldStr,DatabaseCallBack<String> callBack) {
        ProductBrowsingHistoryDao productBrowsingHistoryDao = AppDatabase.getInstance(context).productBrowsingHistoryDao();
        StringBuffer buffer = new StringBuffer();
        mDisposable.add(productBrowsingHistoryDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(productBrowsingHistories -> {
                    if (productBrowsingHistories != null && productBrowsingHistories.size() > 0) {
                        productBrowsingHistoryDao.deleteAll(productBrowsingHistories).subscribe(()->{
                            //删除成功回调
                            callBack.onSuccess(null);
                        });
                    }
                }, throwable -> {
                    Log.e("-----------清空失败", ",清空失败");
                    callBack.onError(throwable);
                }));
        return buffer.toString();
    }

    /**
     * 插入商品ID，查询数据库如果商品ID已经存在，先删除再插入,超过50条先删除再插入
     *
     * @param context     context
     * @param mDisposable mDisposable
     * @param strID       商品ID
     */
    public static void insertProduct(Application context, CompositeDisposable mDisposable, String strID) {
        ProductBrowsingHistoryDao productBrowsingHistoryDao = AppDatabase.getInstance(context).productBrowsingHistoryDao();
        ProductBrowsingHistory newProductBrowsingHistory = new ProductBrowsingHistory();
        newProductBrowsingHistory.productID = strID;
        mDisposable.add(productBrowsingHistoryDao.getAll().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(productBrowsingHistories -> {
                    if (productBrowsingHistories != null && productBrowsingHistories.size() > 0) {
                        List<ProductBrowsingHistory> deleteProductBrowsingHistorys = new ArrayList<>();
                        for (int i = 0; i < productBrowsingHistories.size(); i++) {
                            if(strID.equals(productBrowsingHistories.get(i).productID)||i>=49){
                                deleteProductBrowsingHistorys.add(productBrowsingHistories.get(i));
                            }
                        }
                        mDisposable.add(productBrowsingHistoryDao.deleteAll(deleteProductBrowsingHistorys).subscribe(() -> {
                            productBrowsingHistoryDao.insert(newProductBrowsingHistory);
                        }));
                    } else {
                        productBrowsingHistoryDao.insert(newProductBrowsingHistory);
                    }

                }, throwable -> {
                    Log.e("-----------插入失败", ",插入失败");
                    throwable.printStackTrace();
                }));
    }
}
