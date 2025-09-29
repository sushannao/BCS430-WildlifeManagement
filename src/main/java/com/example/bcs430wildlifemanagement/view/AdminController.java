package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.inventory.RequestsData;
import com.example.bcs430wildlifemanagement.inventory.ResupplyRequest;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.google.cloud.Timestamp;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.*;
import com.example.bcs430wildlifemanagement.model.App;

import java.io.IOException;

public class AdminController {
    @FXML
    private void openViewRequests() {
        try {
            App.setRoot("/com/example/bcs430wildlifemanagement/viewRequests.fxml");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open view requests", ButtonType.OK).showAndWait();
        }
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

}
