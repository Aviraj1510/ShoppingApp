package com.example.shoppinglistjava.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.db.ShoppingCItemDao;
import com.example.shoppinglistjava.db.ShoppingDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingItemRepository {
    private ShoppingCItemDao shoppingCItemDao;
    private ExecutorService executorService;

    public ShoppingItemRepository(Application application) {
        ShoppingDatabase db = ShoppingDatabase.getInstance(application);
        shoppingCItemDao = db.shoppingCItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(final ShoppingCList shoppingCategoryItem) {
        executorService.execute(() -> shoppingCItemDao.insert(shoppingCategoryItem));
    }

    public void delete(final ShoppingCList shoppingCList){
        executorService.execute(() -> shoppingCItemDao.delete(shoppingCList));
    }

    public void update(final ShoppingCList shoppingCList){
        executorService.execute(() -> shoppingCItemDao.update(shoppingCList));
    }

    public LiveData<List<ShoppingCList>> getItemsByCategory(int categoryId) {
        return shoppingCItemDao.getItemsByCategoryId(categoryId);
    }
}
