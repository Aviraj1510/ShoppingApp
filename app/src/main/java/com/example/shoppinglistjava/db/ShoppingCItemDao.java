package com.example.shoppinglistjava.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.ListData.ShoppingItem;

import java.util.List;

@Dao
public interface ShoppingCItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ShoppingCList shoppingCList);

    @Delete
    void delete(ShoppingCList shoppingCList);

    @Update
    void update(ShoppingCList shoppingCList);

    @Query("SELECT * FROM shopping_category_items WHERE categoryId = :categoryId AND userId = :userId")
    LiveData<List<ShoppingCList>> getItemsByCategoryId(int categoryId, String userId);

}
