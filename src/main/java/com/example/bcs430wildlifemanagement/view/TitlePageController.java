package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class TitlePageController {

    public void loginPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/login.fxml");
    }

    public void registerPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Register.fxml");
    }

}
