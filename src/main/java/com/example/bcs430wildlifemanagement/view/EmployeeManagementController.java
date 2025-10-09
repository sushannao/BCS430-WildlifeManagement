package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import javafx.event.ActionEvent;

import java.io.IOException;

public class EmployeeManagementController {

    public void HomePageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Home.fxml");
    }

    public void AnimalsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Animals.fxml");
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
