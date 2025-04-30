package com.example.shoppinglistjava.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.db.CategoryDao;
import com.example.shoppinglistjava.db.ShoppingDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        saveToFirebase(category);
    }
    public void delete(Category category){
        executorService.execute(() -> categoryDao.delete(category));
    }
    public LiveData<List<Category>> getAllCategory(){
        return allCategory;
    }

    private void saveToFirebase(Category item) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Category")
                .child(userId)
                .push();


        reference.setValue(item);
    }

    public void syncFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Category")
                .child(userId);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Category item = itemSnapshot.getValue(Category.class);
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
