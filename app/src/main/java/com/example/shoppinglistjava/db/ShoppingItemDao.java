package com.example.shoppinglistjava.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppinglistjava.ListData.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingItemDao {
    @Insert
    void insert(ShoppingItem item);

    @Update
    void update(ShoppingItem item);

    @Delete
    void delete(ShoppingItem item);

    @Query("SELECT * FROM shopping_items WHERE userId = :userId")
    LiveData<List<ShoppingItem>> getAllShoppingItems(String userId);


    @Query("SELECT * FROM shopping_items WHERE name LIKE :query AND userId = :userId")
    LiveData<List<ShoppingItem>> searchItems(String query, String userId);


}
