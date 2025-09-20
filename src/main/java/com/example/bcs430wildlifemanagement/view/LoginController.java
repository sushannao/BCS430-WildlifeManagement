package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.model.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    public static String getApiKey() {
        Properties prop = new Properties();
        try {
            FileInputStream fileInput = new FileInputStream("config.properties");
            prop.load(fileInput);
            return prop.getProperty("apiKey");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String apiKey = getApiKey();

    public void loginButton(ActionEvent actionEvent) throws IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email and password cannot be empty.");
            return;
        }

        try {
            boolean success = authenticateUser(email, password);
            if (success) {
                UserSession.setEmail(email);
                System.out.println("Login successful!");
                App.setRoot("/com/example/bcs430wildlifemanagement/Home.fxml");
            } else {
                errorLabel.setText("Login failed. Try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("An error occurred during login.");
        }
    }

    public void registerButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Register.fxml");
    }

    private boolean authenticateUser(String email, String password) {
        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey);
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
            } else {
                scanner = new Scanner(conn.getErrorStream(), "utf-8");
            }

            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            System.out.println("Login response: " + response.toString());

            return code == 200;

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login failed. Try again.");
            return false;
        }
    }
    @FXML
    private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password?");
        alert.setHeaderText("If You Forgot Your Password, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }

}
