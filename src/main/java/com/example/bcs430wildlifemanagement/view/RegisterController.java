package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class RegisterController {
    @FXML private TextField emailField;

    @FXML private PasswordField confirmPasswordField;

    @FXML private PasswordField passwordField;

    @FXML private Label errorLabel;

    public void registerButton(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        try {
            registerUser(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during registration.");
        }

    }

    public void loginButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Login.fxml");
    }

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
    }

    private static final String apiKey = getApiKey();

    private void registerUser(String email, String password) {
        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format(
                    "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                    email, password
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();

            Scanner scanner;
            if (code == 200) {
                scanner = new Scanner(conn.getInputStream(), "utf-8");
                errorLabel.setText("Registered successfully. Now Login!");
            } else {
                scanner = new Scanner(conn.getErrorStream(), "utf-8");
                errorLabel.setText("Registration failed. Try again.");
            }

            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            System.out.println("Response: " + response.toString());

       /*     JsonObject userInput = JsonParser.parseString(userInput.toString()).getAsJsonObject();
            String localId = response.get("localId").getAsString();
            String idToken = response.get("idToken").getAsString();

            saveUserInput(localId, idToken); */

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during registration.");
        }

    }
/*
    private void saveUserInput(String localId, String idToken) {
        try {
            String name = nameField.getText();
            String reEntryEmail;
            String phoneNum;

        } */

}
