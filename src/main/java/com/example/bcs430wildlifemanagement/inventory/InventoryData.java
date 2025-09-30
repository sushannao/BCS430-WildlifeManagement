package com.example.bcs430wildlifemanagement.inventory;

import com.google.cloud.firestore.QuerySnapshot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.DocumentSnapshot;

public class InventoryData {
    // gets an instance of thei nventory
    private static final InventoryData INSTANCE = new InventoryData();
    // lists for the ui
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private final ObservableList<InventoryItem> requests = FXCollections.observableArrayList();

    private InventoryData() {
        readInventory();
    }
    public static InventoryData getInstance() {
        return INSTANCE;
    }
    public ObservableList<InventoryItem> getItems() {
        return items;
    }
    /*public ObservableList<InventoryItem> getRequests() {
        return requests;
    }
    public void requestRestock(InventoryItem item) {
        if (item != null && !requests.contains(item)) {
            requests.add(item);
        }
    }*/
    // reads the inventory from firebase database
    private void readInventory() {
        new Thread(() -> {
            try {
                Firestore db = new com.example.bcs430wildlifemanagement.model.FirestoreContext().firebase();
                QuerySnapshot snap = db.collection("inventory").get().get();
                var fresh = new java.util.ArrayList<InventoryItem>();
                for (DocumentSnapshot doc : snap.getDocuments()) {
                    try {
                        // adds the data in the database to the inventory
                        String name = java.util.Objects.toString(doc.get("name"), doc.getId());
                        String catStr = java.util.Objects.toString(doc.get("category"), "");
                        InventoryItem.Category category = parseCategory(catStr);
                        int quantity = toInt(doc.get("quantity"), 0);
                        int lowStock = toInt(doc.get("lowStock"), 0);
                        String unit = java.util.Objects.toString(doc.get("unit"), "");
                        String location = java.util.Objects.toString(doc.get("location"), "");
                        String notes = java.util.Objects.toString(doc.get("notes"), "");
                        InventoryItem item = new InventoryItem(name, category, quantity, lowStock, unit, location, notes);
                        item.setId(doc.getId());
                        fresh.add(item);
                    } catch (Exception ex) {
                        System.err.println("error");
                    }
                }
                javafx.application.Platform.runLater(() -> items.setAll(fresh)); // refresh the inventory table
            } catch (Exception e) {
                javafx.application.Platform.runLater(() ->
                        new javafx.scene.control.Alert(
                                javafx.scene.control.Alert.AlertType.ERROR,"Erorr " + e.getMessage(), javafx.scene.control.ButtonType.OK).showAndWait());
            }
        }, "inv").start();
    }
    // converts an object to an integer
    private static int toInt(Object o, int def) {
        try {
            if (o == null) return def;
            if (o instanceof Number n) return n.intValue();
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return def;
        }
    }
    // converts a string to a category
    private static InventoryItem.Category parseCategory(String s) {
        if (s == null) throw new IllegalArgumentException("no category");
        for (InventoryItem.Category c : InventoryItem.Category.values()) {
            if (c.name().equalsIgnoreCase(s.trim()))
                return c;
        }
        throw new IllegalArgumentException("Wrong cartegory " + s);
    }

}
