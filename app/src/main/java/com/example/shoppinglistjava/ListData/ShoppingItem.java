package com.example.shoppinglistjava.ListData;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int amount;
    private double rupees;
    private double totalrupees;

    public ShoppingItem(String name, int amount, double rupees) {
        this.name = name;
        this.amount = amount;
        this.rupees = rupees;
        this.totalrupees = rupees * amount;
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
