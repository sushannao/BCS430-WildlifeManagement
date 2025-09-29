package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;

public class FormsController {

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

    public void ViewInventoryPageButton(ActionEvent actionEvent) throws IOException{
        App.setRoot("/com/example/bcs430wildlifemanagement/Inventory.fxml");
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
