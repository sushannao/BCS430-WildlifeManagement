package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.model.UserSession;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    private String uid;
    private String idToken;
    private String username;

    // these methods are to get the apiKey from our config.properties file
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

    // this method gets information set if the authenticateuser method is successful
    public void loginButton(ActionEvent actionEvent) throws IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Email and password cannot be empty.");
            return;
        }
        // sets all user session information
        try {
            boolean success = authenticateUser(email, password);
            if (success) {
                UserSession.setEmail(email);
                UserSession.setUid(uid);
                UserSession.setIdToken(idToken);
                displayUserFName();
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

    // register button usage
    public void registerPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Register.fxml");
    }

    // this method uses Http connection to connect to our database and authenticate our users email and password
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

            // this gets a error response code to be used
            int responseCode = conn.getResponseCode();
            Scanner scanner;
            if (responseCode == 200) {
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

            // this uses Json Object to recieve the json file of the user in session so we could save their idtoken and uid
            if (responseCode == 200) {
                JsonObject obj = JsonParser.parseString(response.toString()).getAsJsonObject();
                this.idToken = obj.get("idToken").getAsString();
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                this.uid = decodedToken.getUid();
                return true;
            } else {
                errorLabel.setText("Login failed. Try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login failed.");
            return false;
        }
        return false;
    }

    // this method allows a popup message when clicked contact us
    @FXML private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password?");
        alert.setHeaderText("If You Forgot Your Password, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }

    // this method gets the firstName of users to user session and display it in the home page
    private void displayUserFName() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference reference = db.collection("users").document(uid);
            DocumentSnapshot snapshot = reference.get().get();

            if (snapshot.exists()){
                String fName = snapshot.getString("firstName");
                UserSession.setFName(fName);
            } else {
                System.out.println("Username not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
