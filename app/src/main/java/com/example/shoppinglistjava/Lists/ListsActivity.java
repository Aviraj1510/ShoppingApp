package com.example.shoppinglistjava.Lists;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.CategoryAdapter;
import com.example.shoppinglistjava.ListData.Category;
import com.example.shoppinglistjava.ListData.ShoppingCList;
import com.example.shoppinglistjava.MainActivity;
import com.example.shoppinglistjava.R;
import com.example.shoppinglistjava.ViewModel.CategoryViewModel;
import com.example.shoppinglistjava.ViewModel.ShoppingCategoryItemViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class ListsActivity extends AppCompatActivity {

    private CategoryViewModel categoryViewModel;
    private EditText edtCategory;
    private Button btnCategory;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerViewCategory;
    private ShoppingCategoryItemViewModel shoppingCategoryItemViewModel;
    private static final int CREATE_FILE_REQUEST_CODE = 1001;
    private String tempCategoryContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
        edtCategory = findViewById(R.id.edtCategory);
        btnCategory = findViewById(R.id.btnAddCategory);

        recyclerViewCategory = findViewById(R.id.listsRv);

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        shoppingCategoryItemViewModel = new ViewModelProvider(this).get(ShoppingCategoryItemViewModel.class);


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

            @Override
            public void onSaveClick(Category category) {
                int categoryId = category.getCategoryId();

                shoppingCategoryItemViewModel.getItemsByCategory(categoryId).observe(ListsActivity.this, new Observer<List<ShoppingCList>>() {
                    @Override
                    public void onChanged(List<ShoppingCList> items) {
                        if (items != null && !items.isEmpty()) {
                            // Build the content string
                            StringBuilder dataToSave = new StringBuilder();
                            dataToSave.append("🛒 Shopping Bill\n");
                            dataToSave.append("============================\n");
                            int grandTotal = 0;

                            for (ShoppingCList item : items) {
                                String name = item.getCname();
                                int quantity = item.getCamount();
                                int price = (int) item.getCrupees();
                                int total = quantity * price;

                                grandTotal += total;

                                dataToSave.append("Item: ").append(item.getCname())
                                        .append("\nQty: ").append(item.getCamount())
                                        .append(" x ₹").append(item.getCrupees())
                                        .append("\nTotal: ₹").append(total)
                                        .append("\n----------------------------\n");
                            }

                            dataToSave.append("\nGrand Total: ₹").append(grandTotal);
                            dataToSave.append("\nThank you for shopping with us! 🛍️\n");
                            // Save the content using system file picker
                            createFileAndSave(category.getCategoryName(), dataToSave.toString());

                            // ⚠️ Remove observer after use to prevent multiple calls
                            shoppingCategoryItemViewModel.getItemsByCategory(categoryId).removeObservers(ListsActivity.this);

                        } else {
                            Toast.makeText(ListsActivity.this, "No items found in this category.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                try {
                    OutputStream outputStream = getContentResolver().openOutputStream(fileUri);
                    outputStream.write(tempCategoryContent.getBytes());
                    outputStream.close();
                    Toast.makeText(this, "File saved successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveCategoryItemsToFile(String categoryName, List<ShoppingCList> items) {
        // Convert list to a file format (for example, CSV or text)
        StringBuilder dataToSave = new StringBuilder();
        for (ShoppingCList item : items) {
            // Assuming ShoppingCList has a name field 'cname'
            dataToSave.append(item.getCname()).append("\n")
                    .append(item.getCamount());
        }

        createFileAndSave(categoryName, dataToSave.toString());
    }
    private void createFileAndSave(String categoryName, String fileContent) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, categoryName + ".txt");
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);

        // Save content to use later in onActivityResult
        tempCategoryContent = fileContent;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ListsActivity.this, MainActivity.class));
        finish();
    }
}
