package com.example.shoppinglistjava.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.repository.ShoppingItemRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ShoppingCategoryItemViewModel extends AndroidViewModel {

    private ShoppingItemRepository shoppingItemRepository;

    public ShoppingCategoryItemViewModel(@NonNull Application application) {
        super(application);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            shoppingItemRepository = new ShoppingItemRepository(application, userId);
        } else {
            throw new IllegalStateException("User not logged in");
        }

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
