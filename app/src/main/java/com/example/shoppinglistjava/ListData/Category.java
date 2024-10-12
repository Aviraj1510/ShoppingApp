package com.example.shoppinglistjava.ListData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list_type")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
