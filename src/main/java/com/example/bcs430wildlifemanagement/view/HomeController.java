package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {
    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        String username = UserSession.getUsername();
        welcomeLabel.setText("Welcome " + username + "!");
    }

}
