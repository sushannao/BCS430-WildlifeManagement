package com.example.bcs430wildlifemanagement.inventory;

public class InventoryItem {
    public enum Category {
        Food, Medicine, Cleaning, Equipment, Misc
    }
    private String name;
    private int quantity;
    private Category category;
    private int lowStock; // if the quantity goes below the assigneed number then it will be considered low stock
    private String unit;
    private String location;
    private String notes;

    public InventoryItem(String name, Category category, int quantity, int lowStock, String unit, String location, String notes) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.lowStock = lowStock;
        this.unit = unit;
        this.location = location;
        this.notes = notes;
    }
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

}
