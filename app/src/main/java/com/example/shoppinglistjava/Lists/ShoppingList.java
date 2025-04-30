package com.example.shoppinglistjava.Lists;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistjava.Adapter.ShoppingListAdapter;
import com.example.shoppinglistjava.ListData.ShoppingItem;
import com.example.shoppinglistjava.MainActivity;
import com.example.shoppinglistjava.R;
import com.example.shoppinglistjava.ViewModel.ShoppingViewModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class ShoppingList extends AppCompatActivity {

    private ShoppingViewModel shoppingViewModel;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView rvShoppingList;
    private ImageView imgCart;
    private SearchView searchView;
    private ImageButton imageButton;
    private Button saveBtn;
    private static final int CREATE_FILE_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        searchView = findViewById(R.id.searchView);
        imgCart = findViewById(R.id.imgCart);
        imageButton = findViewById(R.id.btnBack);
        saveBtn = findViewById(R.id.saveList);


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


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBillGeneration();
            }
        });



        shoppingListAdapter = new ShoppingListAdapter(new ShoppingListAdapter.OnItemClickListener(){


            @Override
            public void onDeleteClick(ShoppingItem item) {
                shoppingViewModel.delete(item);
            }

            @Override
            public void onPlusClick(ShoppingItem item) {
                item.setAmount(item.getAmount() + 1);
                shoppingViewModel.update(item);
                onTotleRupee(item);
            }

            @Override
            public void onMinusClick(ShoppingItem item) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                    shoppingViewModel.update(item);
                    onTotleRupee(item);
                }
            }

            @Override
            public void onTotleRupee(ShoppingItem item) {
               double Totle =  item.getRupees() * item.getAmount();
               item.setTotalrupees(Totle);
               shoppingViewModel.update(item);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected file
                Uri uri = data.getData();

                // Now save the bill to the selected file
                try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                    if (outputStream != null) {
                        shoppingViewModel.getAllShoppingItems().observe(this, new Observer<List<ShoppingItem>>() {
                            @Override
                            public void onChanged(List<ShoppingItem> shoppingItems) {
                                if (shoppingItems != null && !shoppingItems.isEmpty()) {
                                    String billData = generateBill(shoppingItems);
                                    try {
                                        outputStream.write(billData.getBytes());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Toast.makeText(ShoppingList.this, "Bill saved to selected file", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShoppingList.this, "No shopping items to save", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ShoppingList.this, "Failed to save bill", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




    private void saveShoppingListToFile() {
        shoppingViewModel.getAllShoppingItems().observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                if (shoppingItems != null && !shoppingItems.isEmpty()) {
                    String billData = generateBill(shoppingItems);

                    // Launch the file picker to let the user choose the location
                    Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TITLE, "shopping_bill.txt");
                    startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
                } else {
                    Toast.makeText(ShoppingList.this, "No shopping items to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void saveShoppingListToExcel() {
        shoppingViewModel.getAllShoppingItems().observe(this, new Observer<List<ShoppingItem>>() {
            @Override
            public void onChanged(List<ShoppingItem> shoppingItems) {
                if (shoppingItems != null && !shoppingItems.isEmpty()) {
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = workbook.createSheet("Shopping List");

                    // Create header row
                    HSSFRow headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Item Name");
                    headerRow.createCell(1).setCellValue("Price");
                    headerRow.createCell(2).setCellValue("Quantity");
                    headerRow.createCell(3).setCellValue("Total");

                    // Add shopping items to the Excel sheet
                    int rowNum = 1;
                    for (ShoppingItem item : shoppingItems) {
                        HSSFRow row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(item.getName());
                        row.createCell(1).setCellValue(item.getRupees());
                        row.createCell(2).setCellValue(item.getAmount());
                        row.createCell(3).setCellValue(item.getTotalrupees());
                    }

                    // Save the file
                    File file = new File(getExternalFilesDir(null), "shopping_list.xls");
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        workbook.write(fos);
                        Toast.makeText(ShoppingList.this, "Excel file saved to " + file.getPath(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ShoppingList.this, "Failed to save Excel file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShoppingList.this, "No shopping items to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String generateBill(List<ShoppingItem> shoppingItems) {
        StringBuilder bill = new StringBuilder();
        bill.append("🛒 Shopping Bill\n");
        bill.append("============================\n");

        double grandTotal = 0;

        for (ShoppingItem item : shoppingItems) {
            double itemTotal = item.getRupees() * item.getAmount();
            bill.append("Item: ").append(item.getName())
                    .append("\nQty: ").append(item.getAmount())
                    .append(" x ₹").append(item.getRupees())
                    .append("\nTotal: ₹").append(itemTotal)
                    .append("\n----------------------------\n");
            grandTotal += itemTotal;
        }

        bill.append("\nGRAND TOTAL: ₹").append(grandTotal).append("\n");
        bill.append("============================\n");
        bill.append("Thank you for shopping with us! 🛍️\n");

        return bill.toString();
    }
    private void confirmBillGeneration() {
        new AlertDialog.Builder(this)
                .setTitle("Generate Bill")
                .setMessage("Do you want to generate the bill?")
                .setPositiveButton("Yes", (dialog, which) -> saveShoppingListToFile())
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ShoppingList.this, MainActivity.class));
        finish();
    }
}