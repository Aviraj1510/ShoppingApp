package com.example.shoppinglistjava.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.repository.ShoppingRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ShoppingViewModel extends AndroidViewModel {
    private ShoppingRepository repository;
    private LiveData<List<ShoppingItem>> allShoppingItems;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>();


    public ShoppingViewModel(@NonNull Application application) {
        super(application);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            repository = new ShoppingRepository(application, userId);
            allShoppingItems = repository.getAllShoppingItems();
        } else {
            throw new IllegalStateException("User not logged in");
        }
    }

    public void insert(ShoppingItem item) {
        repository.insert(item);
    }

    public LiveData<List<ShoppingItem>> getAllShoppingItems() {
        return allShoppingItems;
    }

    public void update(ShoppingItem item) {
        repository.update(item);
    }

    public void delete(ShoppingItem item) {
        repository.delete(item);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public LiveData<List<ShoppingItem>> searchItems(String query) {
        return repository.searchItems(query);
    }

    public void syncFromFirebase() {
        repository.syncFromFirebase();
    }
}
