package com.example.bcs430wildlifemanagement.inventory;

public class InventoryItem {
    public enum Category {
        Food, Medicine, Cleaning, Equipment, Misc // cateory fields
    }
    // saved fields
    private String name;
    private int quantity;
    private Category category;
    private int lowStock; // "low stock" means when the quantity is less than the assigned number, then it would be considered low stock
    private String unit; // lbs/bags/etc
    private String location;
    private String notes;
    private String id; // id fore the firebase database

    // constructor
    public InventoryItem(String name, Category category, int quantity, int lowStock, String unit, String location, String notes) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.lowStock = lowStock;
        this.unit = unit;
        this.location = location;
        this.notes = notes;
    }
    // getters and setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getLowStock() {
        return lowStock;
    }
    public void setLowStock(int lowStock) {
        this.lowStock = lowStock;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
