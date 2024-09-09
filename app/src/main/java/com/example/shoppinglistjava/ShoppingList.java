package com.example.shoppinglistjava;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.ShoppingListAdapter;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class ShoppingList extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView rvShoppingList;
    private ImageView imgCart;
    private SearchView searchView;
    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        searchView = findViewById(R.id.searchView);
        imgCart = findViewById(R.id.imgCart);
        imageButton = findViewById(R.id.btnBack);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingList.this, MainActivity.class));
                finish();
            }
        });


        Animation animation = AnimationUtils.loadAnimation(getApplicationContext()
                , R.anim.cart_anim);
        imgCart.startAnimation(animation);




    // Initialize RecyclerView
        rvShoppingList = findViewById(R.id.rvShoppingList);
        rvShoppingList.setLayoutManager(new LinearLayoutManager(this));


        shoppingListAdapter = new ShoppingListAdapter(new ShoppingListAdapter.OnItemClickListener(){


            @Override
            public void onDeleteClick(ShoppingItem item) {
                shoppingViewModel.delete(item);
            }

            @Override
            public void onPlusClick(ShoppingItem item) {
                item.setAmount(item.getAmount() + 1);
                shoppingViewModel.update(item);
            }

            @Override
            public void onMinusClick(ShoppingItem item) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    shoppingViewModel.update(item);
                }
            }
        });
        rvShoppingList.setAdapter(shoppingListAdapter);

        //Search View

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchShoppingItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchShoppingItems(newText);
                return false;
            }
        });





        // Initialize ViewModel
        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        // Observe the list of shopping items (LiveData)
        shoppingViewModel.getAllShoppingItems().observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                if (shoppingItems != null) {
                    Log.d("ShoppingList", "Items received: " + shoppingItems.size());
                    shoppingListAdapter.setShoppingItems(shoppingItems);
                } else {
                    Log.d("ShoppingList", "No items received");
                }
            }
        });
    }
    private void searchShoppingItems(String query) {
        shoppingViewModel.searchItems(query).observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                if (shoppingItems != null) {
                    shoppingListAdapter.setShoppingItems(shoppingItems);
                }
            }
        });
    }
}