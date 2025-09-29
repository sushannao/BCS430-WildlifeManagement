package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.model.FirestoreContext;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class RegisterController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField confirmEmailField;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private TextField phoneNumField;
    @FXML
    private ComboBox<String> roleBox;


    public void registerButton(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String confirmEmail = confirmEmailField.getText();
        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String phoneNum = phoneNumField.getText();
        String role = roleBox.getValue();

        if (!email.equals(confirmEmail)) {
            errorLabel.setText("Emails do not match.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        if (fName.isEmpty() || lName.isEmpty() || phoneNum.isEmpty() || role == null) {
            errorLabel.setText("All fields must be completed!");
            return;
        }
        try {
            registerUser(email, password, fName, lName, phoneNum, role);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during registration.");
        }
    }

    public void loginPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Login.fxml");
    }

    private void registerUser(String email, String password, String fName, String lName, String phoneNum, String role) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(email).setPassword(password);

            UserRecord userInput = FirebaseAuth.getInstance().createUser(request);
            System.out.println("Successfully created new user: " + userInput.getUid());

            Map<String, Object> userData = new HashMap<>();
            userData.put("firstName", fName);
            userData.put("lastName", lName);
            userData.put("email", email);
            userData.put("phoneNumber", phoneNum);
            userData.put("role", role);

            Firestore db = FirestoreClient.getFirestore();
            DocumentReference reference = db.collection("users").document(userInput.getUid());
            reference.set(userData).get();

            System.out.println("User data saved to Firestore.");
            errorLabel.setText("Registration Successful. Now Login!");

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Employee", "Admin");
    }
}

    /*  private static final String apiKey = getApiKey(); // not needed for now TODO: fix this file for client side

        public static String getApiKey() {
        Properties prop = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream("config.properties");
            prop.load(fileInput);
            System.out.println(prop.getProperty("apiKey"));
            return prop.getProperty("apiKey");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } */