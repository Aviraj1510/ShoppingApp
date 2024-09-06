package com.example.shoppinglistjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;

public class MainActivity extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private EditText etName, etAmount;
    private ImageView cartSlide;
    private Button btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cartSlide = findViewById(R.id.imageView);


        shoppingViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        etName = findViewById(R.id.editTextName);
        etAmount = findViewById(R.id.editAmount);

        Button btnAddItem = findViewById(R.id.buttonAdd);
        Button btnList = findViewById(R.id.btnList);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingList.class);
                startActivity(intent);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();



                if(name.isEmpty() && etAmount.getText().toString().isEmpty()){
                    etName.setError("Please enter an Name");
                    etAmount.setError("Please enter an amount");
                    Toast.makeText(MainActivity.this, "Please enter a name and amount", Toast.LENGTH_SHORT).show();
                }else {
                    int amount = Integer.parseInt(etAmount.getText().toString());
                    ShoppingItem newItem = new ShoppingItem(name, amount);
                    shoppingViewModel.insert(newItem);
                    etName.setText("");
                    etAmount.setText("");
                    etAmount.onEditorAction(EditorInfo.IME_ACTION_DONE);


                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext()
                            , R.anim.cart_anim);
                    cartSlide.startAnimation(animation);
                    cartSlide.setVisibility(View.VISIBLE);

                }

            }
        });
    }
}