package com.example.shoppinglistjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private EditText etName, etAmount, etRupee;
    private ImageView cartSlide;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartSlide = findViewById(R.id.imageView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Button btnAddItem = findViewById(R.id.buttonAdd);

        String[] ProductType = getResources().getStringArray(R.array.Product_Type);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                ProductType
        );
        AutoCompleteTextView autocompleteTV = findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setAdapter(arrayAdapter);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.list_home){
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                }
                if (id == R.id.list_list){
                    startActivity(new Intent(MainActivity.this, ShoppingList.class));
                    return true;
                }
                if (id == R.id.Lists){
                    return true;
                }
                return false;
            }
        });

        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        etName = findViewById(R.id.editTextName);
        etAmount = findViewById(R.id.editAmount);
        etRupee = findViewById(R.id.editRupees);

//Button Click
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String productType = autocompleteTV.getText().toString();


                if(name.isEmpty() && etAmount.getText().toString().isEmpty()){
                    etName.setError("Please enter an Name");
                    etAmount.setError("Please enter an Quantity");
                    etRupee.setError("Please enter Per Item/Kg Value");
                }else {
                    int amount = Integer.parseInt(etAmount.getText().toString());
                    double rupee = Double.parseDouble(etRupee.getText().toString());



                    ShoppingItem newItem = new ShoppingItem(name, amount, rupee, productType);
                    shoppingViewModel.insert(newItem);
                    etName.setText("");
                    etAmount.setText("");
                    etRupee.setText("");
                    autocompleteTV.setText("");
                    etAmount.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    etRupee.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    autocompleteTV.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext()
                            , R.anim.cart_anim);
                    cartSlide.startAnimation(animation);
                    cartSlide.setVisibility(View.VISIBLE);

                }

            }
        });
    }
}