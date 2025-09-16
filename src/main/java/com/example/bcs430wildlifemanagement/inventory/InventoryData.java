package com.example.bcs430wildlifemanagement.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryData {
    private static final InventoryData INSTANCE = new InventoryData();
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private final ObservableList<InventoryItem> requests = FXCollections.observableArrayList();

    private InventoryData() {
        test();
    }
    public static InventoryData getInstance() {
        return INSTANCE;
    }
    public ObservableList<InventoryItem> getItems() {
        return items;
    }
    public ObservableList<InventoryItem> getRequests() {
        return requests;
    }
    public void requestRestock(InventoryItem item) {
        if (item != null && !requests.contains(item)) {
            requests.add(item);
        }
    }
    // this is hard coded for now, the data will be stored in a database and read from here. Leaving it hard coded since the database doesnt exist rn will modify later
    private void test() {
        items.addAll(
                new InventoryItem("Bear Food", InventoryItem.Category.Food, 15, 10, "bags", "Bear Enclosure", "1 bag per day"),
                new InventoryItem("First Aid", InventoryItem.Category.Medicine, 5, 2, "boxes", "Every Enclosure", "Restock when used"),
                new InventoryItem("Disinfectant Wipes", InventoryItem.Category.Cleaning, 10, 5, "boxes", "Employees Lounge", "Do not use when cleaning birds"),
                new InventoryItem("Gloves", InventoryItem.Category.Equipment, 10, 5, "boxes", "Employees Lounge", "Get a new pair per animal")
        );
    }

}
