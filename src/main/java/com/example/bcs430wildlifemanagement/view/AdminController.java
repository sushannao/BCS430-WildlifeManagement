package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.inventory.RequestsData;
import com.example.bcs430wildlifemanagement.inventory.ResupplyRequest;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.google.cloud.Timestamp;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.*;
import com.example.bcs430wildlifemanagement.model.App;

public class AdminController {
    @FXML
    private void openViewRequests() {
        try {
            App.setRoot("/com/example/bcs430wildlifemanagement/viewRequests.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open view requests", ButtonType.OK).showAndWait();
        }
    }
}
