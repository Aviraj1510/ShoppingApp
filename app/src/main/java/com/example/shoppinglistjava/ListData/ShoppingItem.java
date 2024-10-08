package com.example.shoppinglistjava.ListData;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")

public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int amount;
    private double rupees;
    private String productType;
    private double totalrupees;
    private long categoryId;

    public ShoppingItem(String name, int amount, double rupees, String productType) {
        this.name = name;
        this.amount = amount;
        this.rupees = rupees;
        this.productType = productType;
        this.totalrupees = rupees * amount;
    }

    @Ignore
    public ShoppingItem(long categoryId, double totalrupees, String productType, double rupees, int amount, String name) {
        this.categoryId = categoryId;
        this.totalrupees = totalrupees;
        this.productType = productType;
        this.rupees = rupees;
        this.amount = amount;
        this.name = name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public double getTotalrupees() {
        return totalrupees;
    }

    public void setTotalrupees(double totalrupees) {
        this.totalrupees = totalrupees;
    }

    public double getRupees() {
        return rupees;
    }

    public void setRupees(double rupees) {
        this.rupees = rupees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
