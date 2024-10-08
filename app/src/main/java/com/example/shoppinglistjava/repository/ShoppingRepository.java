package com.example.shoppinglistjava.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.db.ShoppingDatabase;
import com.example.shoppinglistjava.db.ShoppingItemDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingRepository {
    private ShoppingItemDao shoppingItemDao;
    private LiveData<List<ShoppingItem>> allShoppingItems;
    private ExecutorService executorService;



    public ShoppingRepository(Application application) {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        shoppingItemDao = database.shoppingItemDao();
        allShoppingItems = shoppingItemDao.getAllShoppingItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ShoppingItem>> searchItems(String query) {
        return shoppingItemDao.searchItems("%" + query + "%");
    }

    public void insert(ShoppingItem item) {
        executorService.execute(() -> shoppingItemDao.insert(item));
    }
    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return allShoppingItems;
    }

    public void update(ShoppingItem item) {
        executorService.execute(() -> shoppingItemDao.update(item));
    }

    public void delete(ShoppingItem item) {
        executorService.execute(() -> shoppingItemDao.delete(item));
    }

}
