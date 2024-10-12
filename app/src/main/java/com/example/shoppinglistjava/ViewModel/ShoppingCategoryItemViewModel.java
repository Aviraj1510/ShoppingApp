package com.example.shoppinglistjava.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.repository.ShoppingItemRepository;

import java.util.List;

public class ShoppingCategoryItemViewModel extends AndroidViewModel {

    private ShoppingItemRepository shoppingItemRepository;

    public ShoppingCategoryItemViewModel(@NonNull Application application) {
        super(application);
        shoppingItemRepository = new ShoppingItemRepository(application);

    }

    public void insert(ShoppingCList shoppingCategoryItem) {
        shoppingItemRepository.insert(shoppingCategoryItem);
    }

    public void delete(ShoppingCList shoppingCList){
        shoppingItemRepository.delete(shoppingCList);
    }

    public void update(ShoppingCList shoppingCList){
        shoppingItemRepository.update(shoppingCList);
    }
    public LiveData<List<ShoppingCList>> getItemsByCategory(int categoryId) {
        return shoppingItemRepository.getItemsByCategory(categoryId);
    }
}
