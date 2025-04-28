package com.example.shoppinglistjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.CategoryAdapter;
import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.ViewModel.CategoryViewModel;

import java.util.List;


public class ListsActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private EditText edtCategory;
    private Button btnCategory;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        edtCategory = findViewById(R.id.edtCategory);
        btnCategory = findViewById(R.id.btnAddCategory);

        recyclerViewCategory = findViewById(R.id.listsRv);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));


        categoryAdapter = new CategoryAdapter(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                // Handle category item click, pass the categoryId to ShoppingListActivity
                Intent intent = new Intent(ListsActivity.this, ShoppingListActivity.class);
                intent.putExtra("categoryId", category.getCategoryId());
                intent.putExtra("categoryName", category.getCategoryName());
                startActivity(intent);
            }

            @Override
            public void deleteCategory(Category category) {
                // Handle delete action for the category
                categoryViewModel.delete(category);
            }
        });
        recyclerViewCategory.setAdapter(categoryAdapter);
        categoryViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(CategoryViewModel.class);

        categoryViewModel.getAllCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null){
                    categoryAdapter.setCategoryList(categories);
                }else {
                    Log.d("CategoryList", "No Item Received");
                }
            }
        });
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CategoryName = edtCategory.getText().toString();

                if (!CategoryName.isEmpty()){
                    Category category = new Category(CategoryName);
                    categoryViewModel.insert(category);

                    edtCategory.setText(""); // Clear the input field
                    edtCategory.onEditorAction(EditorInfo.IME_ACTION_DONE);
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListsActivity.this, MainActivity.class));
        finish();
    }
}
