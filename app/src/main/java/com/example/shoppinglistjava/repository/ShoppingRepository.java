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
        shoppingItemDao = database.userDao();
        allShoppingItems = shoppingItemDao.getAllShoppingItems();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ShoppingItem>> searchItems(String query) {
        return shoppingItemDao.searchItems("%" + query + "%");
    }

    public void insert(ShoppingItem item) {
//        new InsertShoppingItemAsyncTask(userDao).execute(item);
        executorService.execute(() -> shoppingItemDao.insert(item));
    }
    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return allShoppingItems;
    }

    public void update(ShoppingItem item) {
//        new UpdateShoppingItemAsyncTask(userDao).execute(item);
        executorService.execute(() -> shoppingItemDao.update(item));
    }

    public void delete(ShoppingItem item) {
//        new DeleteShoppingItemAsyncTask(userDao).execute(item);
        executorService.execute(() -> shoppingItemDao.delete(item));
    }

//    // AsyncTask for updating
//    private static class UpdateShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
//        private UserDao userDao;
//
//        private UpdateShoppingItemAsyncTask(UserDao userDao) {
//            this.userDao = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(ShoppingItem... shoppingItems) {
//            userDao.update(shoppingItems[0]);
//            return null;
//        }
//    }
//
//    // AsyncTask for deleting
//    private static class DeleteShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
//        private UserDao userDao;
//
//        private DeleteShoppingItemAsyncTask(UserDao userDao) {
//            this.userDao = userDao;
//        }
//
//        @Override
//        protected Void doInBackground(ShoppingItem... shoppingItems) {
//            userDao.delete(shoppingItems[0]);
//            return null;
//        }
//    }
//
//    private static class InsertShoppingItemAsyncTask extends AsyncTask<ShoppingItem, Void, Void> {
//        private UserDao userDao;
//
//        private InsertShoppingItemAsyncTask(UserDao userDao) {
//            this.userDao = userDao;
//        }
//
//
//        @Override
//        protected Void doInBackground(ShoppingItem... shoppingItems) {
//            userDao.insert(shoppingItems[0]);
//            return null;
//        }
//    }
}
