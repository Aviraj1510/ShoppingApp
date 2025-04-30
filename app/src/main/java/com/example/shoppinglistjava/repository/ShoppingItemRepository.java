package com.example.shoppinglistjava.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.db.ShoppingCItemDao;
import com.example.shoppinglistjava.db.ShoppingDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShoppingItemRepository {
    private ShoppingCItemDao shoppingCItemDao;
    private ExecutorService executorService;
    private final String userId;

    public ShoppingItemRepository(Application application, String userId) {
        this.userId = userId;
        ShoppingDatabase db = ShoppingDatabase.getInstance(application);
        shoppingCItemDao = db.shoppingCItemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insert(final ShoppingCList shoppingCategoryItem) {
        shoppingCategoryItem.setUserId(userId);
        executorService.execute(() -> shoppingCItemDao.insert(shoppingCategoryItem));
        saveToFirebase(shoppingCategoryItem);
    }

    public void delete(final ShoppingCList shoppingCList){
        executorService.execute(() -> shoppingCItemDao.delete(shoppingCList));
    }

    public void update(final ShoppingCList shoppingCList){
        executorService.execute(() -> shoppingCItemDao.update(shoppingCList));
    }

    public LiveData<List<ShoppingCList>> getItemsByCategory(int categoryId) {
        return shoppingCItemDao.getItemsByCategoryId(categoryId, userId);
    }

    private void saveToFirebase(ShoppingCList item) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ShoppingCategoryItems")
                .child(userId)
                .push(); // use ID as key

        reference.setValue(item);
    }

    public void syncFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ShoppingCategoryItems")
                .child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ShoppingCList item = itemSnapshot.getValue(ShoppingCList.class);
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
