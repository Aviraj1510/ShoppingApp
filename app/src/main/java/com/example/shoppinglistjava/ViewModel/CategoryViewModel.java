package com.example.shoppinglistjava.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private LiveData<List<Category>> allCategory;

    // Constructor that takes Application as parameter
    public CategoryViewModel(Application application) {
        super(application); // Call the AndroidViewModel constructor
        categoryRepository = new CategoryRepository(application);
        allCategory = categoryRepository.getAllCategory();
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
}
