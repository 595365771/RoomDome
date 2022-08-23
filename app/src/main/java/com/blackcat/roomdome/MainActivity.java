package com.blackcat.roomdome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ProductBrowsingHistoryDao productBrowsingHistoryDao;
    int pageNumber = 1;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_bind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        productBrowsingHistoryDao = AppDatabase.getInstance(getApplicationContext()).productBrowsingHistoryDao();
                    }
                }).start();

            }
        });
        findViewById(R.id.tv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ProductBrowsingHistory productID = new ProductBrowsingHistory();
                        productID.productID ="2342424";
                        productBrowsingHistoryDao.insert(productID);
                    }
                }).start();
            }
        });
        findViewById(R.id.tv_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable disposable =productBrowsingHistoryDao.getAll().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(productIDS->{
                            productBrowsingHistoryDao.deleteAll(productIDS).subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.io()).subscribe(()->{
                                Log.e("----", "完成");
                            });
                        },e->{

                        });
            }
        });
        findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productBrowsingHistoryDao.getAll().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new SingleObserver<List<ProductBrowsingHistory>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<ProductBrowsingHistory> productIDS) {
                                productIDS.get(0).productID="xxxxgggww11";
                                productBrowsingHistoryDao.update(productIDS.get(0));

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });

            }
        });
        findViewById(R.id.tv_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                productBrowsingHistoryDao.getAll().subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new SingleObserver<List<ProductBrowsingHistory>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<ProductBrowsingHistory> productIDS) {
                                Log.e("----", "onClick: tv_query" + productIDS.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
            }
        });

        findViewById(R.id.tv_query2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productBrowsingHistoryDao.queryProductLimit(pageNumber).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new SingleObserver<List<ProductBrowsingHistory>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onSuccess(@NonNull List<ProductBrowsingHistory> productIDS) {
                                if(productIDS!=null&&productIDS.size()>0){
                                    pageNumber++;
                                }
                                Log.e("----", "onClick: tv_query" + productIDS.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        });
            }
        });
        findViewById(R.id.tv_add_one_hundred).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            ProductBrowsingHistory productID = new ProductBrowsingHistory();
                            productID.productID="AAAAA"+i;
                            productBrowsingHistoryDao.insert(productID);
                        }
                    }
                }).start();
            }
        });


    }
}