package com.blackcat.roomdome;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductBrowsingHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "product_id")
    public String productID;

    @Override
    public String toString() {
        return "ProductID{" +
                "id=" + id +
                ", productID='" + productID + '\'' +
                '}';
    }
}
