package com.example.shoppinglistjava.ListData;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_category_items",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "categoryId",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE))
public class ShoppingCList {

    @PrimaryKey(autoGenerate = true)
    private int itemid;
    private String Cname;
    private int Camount;
    private double Crupees;
    private String CproductType;
    private double Ctotalrupees;
    private int categoryId;
    private int id;
    public String userId;


    public ShoppingCList() {
    }

    public ShoppingCList(String cname, int camount, double crupees, String cproductType, double ctotalrupees, int categoryId) {
        Cname = cname;
        Camount = camount;
        Crupees = crupees;
        CproductType = cproductType;
        Ctotalrupees = ctotalrupees;
        this.categoryId = categoryId;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public int getCamount() {
        return Camount;
    }

    public void setCamount(int camount) {
        Camount = camount;
    }

    public double getCrupees() {
        return Crupees;
    }

    public void setCrupees(double crupees) {
        Crupees = crupees;
    }

    public String getCproductType() {
        return CproductType;
    }

    public void setCproductType(String cproductType) {
        CproductType = cproductType;
    }

    public double getCtotalrupees() {
        return Ctotalrupees;
    }

    public void setCtotalrupees(double ctotalrupees) {
        Ctotalrupees = ctotalrupees;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

