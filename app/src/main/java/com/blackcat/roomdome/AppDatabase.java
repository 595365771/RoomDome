package com.blackcat.roomdome;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ProductBrowsingHistory.class}, version = 1,exportSchema=false)
abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public static final String DATABASE_NAME="jx_room_db";
    public static AppDatabase getInstance(Context context) {
        if(instance==null){
            synchronized (AppDatabase.class){
                instance =Room.databaseBuilder(context,
                        AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        return instance;
    }

    public abstract ProductBrowsingHistoryDao productBrowsingHistoryDao();
}
