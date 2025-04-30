package com.example.shoppinglistjava.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.db.ShoppingDatabase;
import com.example.shoppinglistjava.db.ShoppingItemDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingRepository {
    private ShoppingItemDao shoppingItemDao;
    private LiveData<List<ShoppingItem>> allShoppingItems;
    private ExecutorService executorService;

    private final String userId;

    public ShoppingRepository(Application application, String userId) {
        ShoppingDatabase database = ShoppingDatabase.getInstance(application);
        shoppingItemDao = database.shoppingItemDao();
        this.userId = userId;
        allShoppingItems = shoppingItemDao.getAllShoppingItems(userId);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ShoppingItem>> searchItems(String query) {
        return shoppingItemDao.searchItems("%" + query + "%", userId);
    }

    public void insert(ShoppingItem item) {
        item.setUserId(userId);
        executorService.execute(() -> shoppingItemDao.insert(item));
        saveToFirebase(item);
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

    private void saveToFirebase(ShoppingItem item) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ShoppingItems")
                .child(userId)
                .push();

        reference.setValue(item);
    }

    public void syncFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ShoppingItems")
                .child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ShoppingItem item = itemSnapshot.getValue(ShoppingItem.class);
                    if (item != null) {
                        insert(item); // Save to local Room DB
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseSync", "Failed to sync ShoppingCList: ", error.toException());
            }
        });
    }

}
