package com.example.shoppinglistjava;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.ShoppingListAdapter;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;

import java.util.List;


public class ShoppingList extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView rvShoppingList;
    private ImageView imgCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        imgCart = findViewById(R.id.imgCart);
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
}