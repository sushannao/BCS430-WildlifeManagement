package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.model.UserSession;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class SettingsController {
    @FXML private TextField emailField;
    @FXML private TextField phoneNumField;
    @FXML private TextArea skillsField;
    @FXML private TextArea limitsField;
    @FXML private Label errorLabel;

    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;

    String uid = UserSession.getUid();

    public void logoutButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Login.fxml");
    }
    public void homePageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcswildlifemanagement/Home.fxml");
    }

    @FXML public void initialize() {
        displayUserData();
    }

    private void displayUserData() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference docRef = db.collection("users").document(uid);
            DocumentSnapshot snapshot = docRef.get().get();

            if (snapshot.exists()){
                String email = FirebaseAuth.getInstance().getUser(uid).getEmail();
                String phoneNum = snapshot.getString("phoneNumber");
                String skills = snapshot.getString("skills");
                String limits = snapshot.getString("limitations");

                emailField.setText(email);
                phoneNumField.setText(phoneNum);
                skillsField.setText(skills);
                limitsField.setText(limits);
            } else {
                errorLabel.setText("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load previous data.");
        }
    }

    public void updateButton(ActionEvent actionEvent) throws IOException, FirebaseAuthException {
        String newEmail = emailField.getText();
        String newPhoneNum = phoneNumField.getText();
        String newSkills = skillsField.getText();
        String newLimits = limitsField.getText();

        if (uid == null) {
            errorLabel.setText("Can't find user. Log out and log back in.");
            return;
        }
        if (!newEmail.isEmpty()) {
            UserRecord.UpdateRequest rq = new UserRecord.UpdateRequest(uid).setEmail(newEmail);
            FirebaseAuth.getInstance().updateUser(rq);
        }

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(uid);

        Map<String, Object> update = new HashMap<>();
        update.put("phoneNumber", newPhoneNum);
        update.put("skills", newSkills);
        update.put("limitations", newLimits);

        try {
            docRef.update(update).get();
            System.out.println("Update Successful.");
            errorLabel.setText("Update Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Update Failed. Try again or contact Admin.");
        }
    }

    public void updateAvailButton(ActionEvent actionEvent) throws IOException {

    }

    @FXML private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your profile not updating?");
        alert.setHeaderText("Try Again! If it still doesn't work, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }

    public static String getIdToken (String email, String password) throws IOException {
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
        if (conn.getResponseCode() == 200) {
            try (InputStream is = conn.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is, "utf-8");
                 BufferedReader br = new BufferedReader(isr)) {

                StringBuilder response = new StringBuilder();
                String line;
                     while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                JsonObject obj = JsonParser.parseString(response.toString()).getAsJsonObject();
                return obj.get("idToken").getAsString();
            }
        } else {
            try (InputStream is = conn.getErrorStream();
                 Scanner scanner = new Scanner(is, "utf-8")) {
                StringBuilder error = new StringBuilder();
                while (scanner.hasNextLine()) {
                    error.append(scanner.nextLine());
                }
                System.out.println("Error" + error);
            }
            return null;
        }
    }

    public void changePasswordButton(ActionEvent actionEvent) throws IOException {
        String password = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!newPassword.equals(confirmNewPassword)) {
            errorLabel.setText("New passwords do not match.");
            return;
        }
        if (password.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            errorLabel.setText("Fill out all fields to change passwords");
            return;
        }

        String email = UserSession.getEmail();
        String idToken = getIdToken(email, password);

        if (idToken == null) {
            errorLabel.setText("Current password is incorrect.");
            return;
        }
        changePassword(idToken, newPassword);
        errorLabel.setText("Successful change!");
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

    public void changePassword(String idToken, String newPassword) throws IOException {
        URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:update?key=" + apiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"idToken\":\"%s\",\"password\":\"%s\"}", idToken, newPassword);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        if (conn.getResponseCode() == 200) {
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Failed to change password.");
        }
    }
}
