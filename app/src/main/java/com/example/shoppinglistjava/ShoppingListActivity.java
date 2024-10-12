package com.example.shoppinglistjava;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.ShoppingCategoryListAdapter;
import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.ViewModel.ShoppingCategoryItemViewModel;

import java.util.List;


public class ShoppingListActivity extends AppCompatActivity {

    private String categoryName;
    private int categoryId;
    private ShoppingCategoryItemViewModel shoppingCategoryItemViewModel;
    EditText edtCListName, edtCListQuantity, edtCListRupee;
    Button btnAddCList;
    private RecyclerView recyclerView;
    private ShoppingCategoryListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list2);

        categoryName = getIntent().getStringExtra("categoryName");
        categoryId = getIntent().getIntExtra("categoryId", -1);
        edtCListName = findViewById(R.id.editCListName);
        btnAddCList = findViewById(R.id.buttonAddCategoryList);
        edtCListQuantity = findViewById(R.id.editCListQuantity);
        edtCListRupee = findViewById(R.id.editCListRupee);


        recyclerView = findViewById(R.id.listCategory);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ShoppingCategoryListAdapter(new ShoppingCategoryListAdapter.OnItemClickListener(){

            @Override
            public void onPlusClick(ShoppingCList shoppingCList) {
                shoppingCList.setCamount(shoppingCList.getCamount() + 1);
                shoppingCategoryItemViewModel.update(shoppingCList);
                onTotalRupee(shoppingCList);
            }

            @Override
            public void onMinusClick(ShoppingCList shoppingCList) {
                if (shoppingCList.getCamount() > 1) {
                    shoppingCList.setCamount(shoppingCList.getCamount() - 1);
                    shoppingCategoryItemViewModel.update(shoppingCList);
                    onTotalRupee(shoppingCList);
                }
            }

            @Override
            public void onDeleteClick(ShoppingCList shoppingCList) {
                shoppingCategoryItemViewModel.delete(shoppingCList);
            }

            @Override
            public void onTotalRupee(ShoppingCList shoppingCList) {
                double Totle =  shoppingCList.getCrupees() * shoppingCList.getCamount();
                shoppingCList.setCtotalrupees(Totle);
                shoppingCategoryItemViewModel.update(shoppingCList);
            }
        });



        recyclerView.setAdapter(adapter);

        shoppingCategoryItemViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(ShoppingCategoryItemViewModel.class);

        shoppingCategoryItemViewModel.getItemsByCategory(categoryId).observe(this, new Observer<List<ShoppingCList>>() {
            @Override
            public void onChanged(List<ShoppingCList> shoppingItems) {
                adapter.setShoppingItemList(shoppingItems);
            }
        });

        btnAddCList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = edtCListName.getText().toString();
                String quantityStr = edtCListQuantity.getText().toString();
                String rupeeStr = edtCListRupee.getText().toString();

                // Input validation to avoid crashes
                if (!TextUtils.isEmpty(itemName) && !TextUtils.isEmpty(quantityStr) && !TextUtils.isEmpty(rupeeStr) && categoryId != -1) {
                    int itemQuantity = Integer.parseInt(quantityStr);
                    double itemRupee = Double.parseDouble(rupeeStr); // Use Double here

                    // Create a new ShoppingItem with the categoryId
                    ShoppingCList shoppingItem = new ShoppingCList(itemName, itemQuantity, itemRupee, "", 0.0, categoryId);
                    shoppingCategoryItemViewModel.insert(shoppingItem);

                    // Clear input fields after adding
                    edtCListName.setText("");
                    edtCListQuantity.setText("");
                    edtCListRupee.setText("");
                } else {
                    // Optionally show a message if input is invalid
                    Toast.makeText(ShoppingListActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}