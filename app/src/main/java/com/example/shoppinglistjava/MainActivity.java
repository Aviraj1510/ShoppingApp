package com.example.shoppinglistjava;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoppinglistjava.Activity.SettingsActivity;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.Lists.ListsActivity;
import com.example.shoppinglistjava.Lists.ShoppingList;
import com.example.shoppinglistjava.Login.LoginActivity;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private ImageView cartSlide, addItemIcon, btnSettings;
    private CardView cardAdd;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartSlide = findViewById(R.id.imageView);
        addItemIcon = findViewById(R.id.addItemIcon);
        cardAdd = findViewById(R.id.cardAdd);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        btnSettings = findViewById(R.id.btnSettings);


        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish();
        });

        Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.click_animation);
        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.list_home) {
                    return true;
                } else if (id == R.id.list_list) {
                    startActivity(new Intent(MainActivity.this, ShoppingList.class));
                    finish();
                    return true;
                } else if (id == R.id.Lists) {
                    startActivity(new Intent(MainActivity.this, ListsActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

        cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(clickAnimation);
                v.postDelayed(() -> {
                    openAddItemDialog();
                    Toast.makeText(MainActivity.this, "Add Item Clicked!", Toast.LENGTH_SHORT).show();
                }, 200);
            }
        });

    }

    private void openAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);

        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.editTextName);
        EditText etAmount = dialogView.findViewById(R.id.editAmount);
        EditText etRupee = dialogView.findViewById(R.id.editRupees);
        Button buttonAdd = dialogView.findViewById(R.id.buttonAdd); // Custom button in layout
        AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.autoCompleteTextView);

        String[] ProductType = getResources().getStringArray(R.array.Product_Type);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, ProductType);
        autoCompleteTextView.setAdapter(arrayAdapter);

        AlertDialog dialog = builder.create();
        dialog.show(); // Show dialog first before setting button click listener
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        buttonAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String productType = autoCompleteTextView.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String rupeeStr = etRupee.getText().toString().trim();

            if (name.isEmpty() || amountStr.isEmpty() || rupeeStr.isEmpty() || productType.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                int amount = Integer.parseInt(amountStr);
                double rupee = Double.parseDouble(rupeeStr);

                ShoppingItem newItem = new ShoppingItem(name, amount, rupee, productType);
                shoppingViewModel.insert(newItem);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cart_anim);
                cartSlide.startAnimation(animation);
                cartSlide.setVisibility(View.VISIBLE);

                dialog.dismiss(); // Close dialog after adding
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
