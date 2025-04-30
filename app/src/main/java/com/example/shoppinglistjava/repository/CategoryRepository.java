package com.example.shoppinglistjava.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.db.CategoryDao;
import com.example.shoppinglistjava.db.ShoppingDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategory;
    private ExecutorService executorService;
    private final String userId;
    public CategoryRepository(Application application, String userId){
        ShoppingDatabase shoppingDatabase = ShoppingDatabase.getInstance(application);
        categoryDao = shoppingDatabase.categoryDao();
        this.userId = userId;
        allCategory = categoryDao.getAllCategories(userId);
        executorService = Executors.newSingleThreadExecutor();

    }
    public void insert(Category category){
        category.setUserId(userId);
        executorService.execute(() -> categoryDao.insert(category));
    }
    public void delete(Category category){
        executorService.execute(() -> categoryDao.delete(category));
    }
    public LiveData<List<Category>> getAllCategory(){
        return allCategory;
    }
}
