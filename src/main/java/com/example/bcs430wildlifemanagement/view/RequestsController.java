package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.inventory.RequestsData;
import com.example.bcs430wildlifemanagement.inventory.ResupplyRequest;
import com.google.cloud.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.bcs430wildlifemanagement.model.App;

public class RequestsController {
    private final RequestsData data = RequestsData.getInstance();
    @FXML
    private TableView<ResupplyRequest> table;
    @FXML
    private TableColumn<ResupplyRequest, String> itemColumn;
    @FXML
    private TableColumn<ResupplyRequest, Number> quantityColumn;
    @FXML
    private TableColumn<ResupplyRequest, String> unitColumn;
    @FXML
    private TableColumn<ResupplyRequest, String> nameColumn;
    @FXML
    private TableColumn<ResupplyRequest, String> emailColumn;
    @FXML
    private TableColumn<ResupplyRequest, String> notesColumn;
    @FXML
    private TableColumn<ResupplyRequest, String> createdColumn;

    @FXML
    public void initialize() {
        table.setItems(data.getRequests());
        itemColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getItemName()));
        quantityColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantityRequested()));
        unitColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUnit()));
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRequesterName()));
        emailColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRequestedEmail()));
        notesColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNotes()));
        createdColumn.setCellValueFactory(c -> new SimpleStringProperty(formatTime(c.getValue().getCreated())));
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private String formatTime(Timestamp time) {
        return time == null ? "": time.toDate().toString();
    }

    @FXML
    private void fulfillRequest() {
        ResupplyRequest request = table.getSelectionModel().getSelectedItem();
        if (request == null) {
            showInfo("No request selected");
            return;
        }
        TextInputDialog dialog = new TextInputDialog(String.valueOf(request.getQuantityRequested()));
        dialog.setTitle("Fulfill Request");
        dialog.setHeaderText("How many " + request.getItemName() + " do you need?");
        dialog.setContentText("Quantity: ");
        var result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        int add;
        try { add = Integer.parseInt(result.get());
            if (add < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            error("Invalid quantity");
            return;
        }
        try {
            data.fulfillRequest(request.getId(), request.getItemId(), add);
            showInfo("Request fulfilled");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    @FXML
    private void backToAdmin() {
        try {
            App.setRoot("/com/example/bcs430wildlifemanagement/admin.fxml");
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void showInfo(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    private void error(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }
}
