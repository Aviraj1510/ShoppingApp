package com.example.shoppinglistjava.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.ListData.ShoppingItem;

import java.util.Locale;


@Database(entities = {ShoppingItem.class, Category.class, ShoppingCList.class}, version = 10)
public abstract class ShoppingDatabase extends RoomDatabase {


    private static ShoppingDatabase instance;
    public abstract ShoppingItemDao shoppingItemDao();
    public abstract CategoryDao categoryDao();
    public abstract ShoppingCItemDao shoppingCItemDao();


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
