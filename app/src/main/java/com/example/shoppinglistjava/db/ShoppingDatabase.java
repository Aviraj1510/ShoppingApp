package com.example.shoppinglistjava.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.shoppinglistjava.ListData.ShoppingItem;


@Database(entities = {ShoppingItem.class}, version = 2)
public abstract class ShoppingDatabase extends RoomDatabase {


    private static ShoppingDatabase instance;
    public abstract ShoppingItemDao shoppingItemDao();


    public static synchronized ShoppingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingDatabase.class, "shopping_database.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
