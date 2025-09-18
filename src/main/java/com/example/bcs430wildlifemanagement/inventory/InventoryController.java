package com.example.bcs430wildlifemanagement.inventory;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.stream.Collectors;

public class InventoryController {
    private final InventoryData data = InventoryData.getInstance();
    private final FilteredList<InventoryItem> items = new FilteredList<>(data.getItems(), it -> true);

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

    @FXML
    public void invRequestSelected() {
        InventoryItem selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Nothing selected");
            return;
        }
        data.requestRestock(selected);
        showInfo("Restock requested: " + selected.getName());
    }
    @FXML
    public void invViewRequests() {
        String text = data.getRequests().isEmpty() ? "Nso request" :
                data.getRequests().stream().map(InventoryItem::getName).collect(Collectors.joining(", "));

        showInfo(text);
    }
    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
