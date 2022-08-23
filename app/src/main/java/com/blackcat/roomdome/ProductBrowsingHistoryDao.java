package com.blackcat.roomdome;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ProductBrowsingHistoryDao {
    @Query("SELECT * FROM productBrowsingHistory  ORDER BY id DESC LIMIT (:pageNumber-1)*20,20")
    Single<List<ProductBrowsingHistory>> queryProductLimit(int pageNumber);

    @Query("SELECT * FROM productBrowsingHistory WHERE product_id=:productID")
    Single<List<ProductBrowsingHistory>> queryProductByID(String productID);

    @Query("SELECT * FROM productBrowsingHistory")
    Single<List<ProductBrowsingHistory>> getAll();

    @Insert
    void insert(ProductBrowsingHistory productID);

    @Delete
    Completable deleteAll(List<ProductBrowsingHistory> productIDs);
    @Delete
    Completable delete(ProductBrowsingHistory productID);
    @Update
    void update(ProductBrowsingHistory productID);
}
