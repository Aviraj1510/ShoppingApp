package com.example.shoppinglistjava.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.repository.CategoryRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategory;

    // Constructor that takes Application as parameter
    public CategoryViewModel(Application application) {
        super(application); // Call the AndroidViewModel constructor
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            categoryRepository = new CategoryRepository(application, userId);
            allCategory = categoryRepository.getAllCategory();
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }

    // Insert method
    public void insert(Category item) {
        categoryRepository.insert(item);
    }

    public void delete(Category item){
        categoryRepository.delete(item);
    }

    // Method to get all categories
    public LiveData<List<Category>> getAllCategory() {
        return allCategory;
    }
    public void syncFromFirebase() {
        categoryRepository.syncFromFirebase();
    }
}
