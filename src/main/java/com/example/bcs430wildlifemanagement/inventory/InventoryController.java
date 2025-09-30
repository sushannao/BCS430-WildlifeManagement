package com.example.bcs430wildlifemanagement.inventory;


import com.example.bcs430wildlifemanagement.model.App;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.stream.Collectors;
import com.example.bcs430wildlifemanagement.model.FirestoreContext;

public class InventoryController {
    private final InventoryData data = InventoryData.getInstance(); // holds the inventory data
    private final FilteredList<InventoryItem> items = new FilteredList<>(data.getItems(), it -> true); // lets us filter in the inventory

    // table fields for the inventory
    @FXML
    private TableView<InventoryItem> table;
    @FXML
    private TableColumn<InventoryItem, String> nameColumn;
    @FXML
    private TableColumn<InventoryItem, String> categoryColumn;
    @FXML
    private TableColumn<InventoryItem, Integer> quantityColumn;
    @FXML
    private TableColumn<InventoryItem, String> unitColumn;
    @FXML
    private TableColumn<InventoryItem, String> locationColumn;

    // checks table fields then displays in cells
    @FXML
    public void initialize() {
        if (nameColumn != null) {
            nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
            categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory().name()));
            quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
            unitColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUnit()));
            locationColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLocation()));
        }
        if (table != null) {
            table.setItems(items);
            table.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
            table.getSelectionModel().setCellSelectionEnabled(false);
        }
    }

    // checks if inventory item is selected and prompts users for quantity and notes
    @FXML
    public void invRequestSelected() {
        InventoryItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Nothing selected");
            return;
        }
        if (selected.getId() == null || selected.getId().isBlank()) {
            showInfo("Item has no id");
            return;
        }
        //
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Request Resupply");
        dialog.setHeaderText("How many " + selected.getName() + " do you need?");
        dialog.setContentText("Quantity: ");
        var result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        // saves number into qty
        int qty;
        try {
            qty = Integer.parseInt(result.get());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity", ButtonType.OK).showAndWait();
            return;
        }
        // Notes (not required, things like "low stock" wouldnt really need a note)
        TextInputDialog notesBox = new TextInputDialog("");
        notesBox.setTitle("Notes");
        notesBox.setHeaderText("Optional");
        notesBox.setContentText("Notes: ");
        String notes = notesBox.showAndWait().orElse("");
        // send the request to firebase
        try {
            var db = FirestoreContext.firebase();
            var data = new java.util.HashMap<String, Object>();
            data.put("itemId", selected.getId());
            data.put("itemName", selected.getName());
            data.put("quantityRequested", qty);
            data.put("unit", selected.getUnit());
            data.put("notes", notes);
            //Gets the current quantity, # considered to be low stock, units, and the locatoin of the item
            var currentInv = new java.util.HashMap<String, Object>();
            currentInv.put("quantity", selected.getQuantity());
            currentInv.put("lowStock", selected.getLowStock());
            currentInv.put("unit", selected.getUnit());
            currentInv.put("location", selected.getLocation());
            data.put("currentInventory", currentInv);
            // get the timestamp of request and the requester info
            data.put("Timestamp: ", com.google.cloud.firestore.FieldValue.serverTimestamp());
            data.put("requestedEmail", com.example.bcs430wildlifemanagement.model.UserSession.getEmail());
            data.put("requesterName", com.example.bcs430wildlifemanagement.model.UserSession.getUid());
            // adds to the resupplyRequests collection
            db.collection("resupplyRequests").add(data).get();
            showInfo("Request sent for " + selected.getName());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error sending request", ButtonType.OK).showAndWait();
        }
    }
    // add item button that prompts the user to add an item
    @FXML
    private void onAddItem() {
        Dialog<InventoryItem> dialog = new Dialog<>();
        ButtonType add = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(add, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField name = new TextField();
        ComboBox<InventoryItem.Category> category = new ComboBox<>();
        category.getItems().setAll(InventoryItem.Category.values());
        category.getSelectionModel().selectFirst();

        TextField quantity = new TextField();
        TextField lowStock = new TextField();
        TextField unit = new TextField();
        TextField location = new TextField();
        TextField notes = new TextField();
        int row = 0;
        grid.addRow(row++, new Label("Name"), name);
        grid.addRow(row++, new Label("Category"), category);
        grid.addRow(row++, new Label("Quantity"), quantity);
        grid.addRow(row++, new Label("Low Stock"), lowStock);
        grid.addRow(row++, new Label("Unit"), unit);
        grid.addRow(row++, new Label("Location"), location);
        grid.addRow(row++, new Label("Notes"), notes);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            // If the add button gets pressed then an item gets created into inventoryItem
            if (btn == add) {
                try{
                    int quant = Integer.parseInt(quantity.getText());
                    int low = Integer.parseInt(lowStock.getText());
                    return new InventoryItem(name.getText(), category.getValue(), quant, low, unit.getText(), location.getText(), notes.getText());
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "error in input", ButtonType.OK).showAndWait();
                }
            }
            return null;
        });
        dialog.showAndWait().ifPresent(item -> {
            try {
                // write to firebase database and add it to inventory
                var db = com.example.bcs430wildlifemanagement.model.FirestoreContext.firebase();
                var doc = new java.util.HashMap<String, Object>();
                doc.put("name", item.getName());
                doc.put("category", item.getCategory().name());
                doc.put("quantity", item.getQuantity());
                doc.put("lowStock", item.getLowStock());
                doc.put("unit", item.getUnit());
                doc.put("location", item.getLocation());
                doc.put("notes", item.getNotes());
                String docId = item.getName();
                db.collection("inventory").document(docId).set(doc); // add to inventory collection
                data.getItems().add(item);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "failed to save", ButtonType.OK).showAndWait();
            }
        });

    }
    // displays info message popup
    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    public void HomePageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Home.fxml");
    }

    public void FormsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Forms.fxml");
    }

    public void AdminPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Admin.fxml");
    }

    public void SettingsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Settings.fxml");
    }

    @FXML
    private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Need Help?");
        alert.setHeaderText("If You Need Help, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }
}
