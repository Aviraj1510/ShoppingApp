package com.example.shoppinglistjava.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.shoppinglistjava.ListData.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM list_type WHERE userId = :userId")
    LiveData<List<Category>> getAllCategories(String userId);



}

