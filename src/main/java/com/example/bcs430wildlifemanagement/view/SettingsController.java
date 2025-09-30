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

    @FXML private TextField sundayField;
    @FXML private TextField mondayField;
    @FXML private TextField tuesdayField;
    @FXML private TextField wednesdayField;
    @FXML private TextField thursdayField;
    @FXML private TextField fridayField;
    @FXML private TextField saturdayField;

    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;

    // getting the uid and idtoken from user session after login
    String uid = UserSession.getUid();
    String idToken = UserSession.getIdToken();

    // all action buttons from the menu navigation
    public void homePageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Home.fxml");
    }
    public void formsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Forms.fxml");
    }
    public void adminPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Admin.fxml");
    }
    public void settingsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Settings.fxml");
    }
    public void logoutButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Login.fxml");
    }

    // these 2 methods allows firebase to get a document reference to display data by prompting the text fields
    @FXML public void initialize() {
        displayUserData();
    }

    private void displayUserData() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            DocumentReference reference = db.collection("users").document(uid);
            DocumentSnapshot snapshot = reference.get().get();

            if (snapshot.exists()){
                String email = FirebaseAuth.getInstance().getUser(uid).getEmail();
                String phoneNum = snapshot.getString("phoneNumber");
                String skills = snapshot.getString("skills");
                String limits = snapshot.getString("limitations");
                String sunday = snapshot.getString("Availability Sunday");
                String monday = snapshot.getString("Availability Monday");
                String tuesday = snapshot.getString("Availability Tuesday");
                String wednesday = snapshot.getString("Availability Wednesday");
                String thursday = snapshot.getString("Availability Thursday");
                String friday = snapshot.getString("Availability Friday");
                String saturday = snapshot.getString("Availability Saturday");

                emailField.setText(email);
                phoneNumField.setText(phoneNum);
                skillsField.setText(skills);
                limitsField.setText(limits);
                sundayField.setText(sunday);
                mondayField.setText(monday);
                tuesdayField.setText(tuesday);
                wednesdayField.setText(wednesday);
                thursdayField.setText(thursday);
                fridayField.setText(friday);
                saturdayField.setText(saturday);
            } else {
                errorLabel.setText("User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load previous data.");
        }
    }

    // this method updates the database from users, and also recieves email from firebase auth to update
    public void updateProfileButton(ActionEvent actionEvent) throws IOException, FirebaseAuthException {
        String newEmail = emailField.getText();
        String newPhoneNum = phoneNumField.getText();
        String newSkills = skillsField.getText();
        String newLimits = limitsField.getText();

        if (uid == null) {
            errorLabel.setText("Can't find user. Log out and log back in.");
            return;
        }
        if (!newEmail.isEmpty()) {
            UserRecord.UpdateRequest userRq = new UserRecord.UpdateRequest(uid).setEmail(newEmail);
            FirebaseAuth.getInstance().updateUser(userRq);
        }

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference reference = db.collection("users").document(uid);

        Map<String, Object> update = new HashMap<>();
        update.put("phoneNumber", newPhoneNum);
        update.put("skills", newSkills);
        update.put("limitations", newLimits);

        try {
            reference.update(update).get();
            System.out.println("Update Successful.");
            errorLabel.setText("Update Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Update Failed. Try again or contact Admin.");
        }
    }

    // this method updates the availability fields and updates the database
    public void updateAvailButton(ActionEvent actionEvent) throws IOException {
        String sunday = sundayField.getText();
        String monday = mondayField.getText();
        String tuesday = tuesdayField.getText();
        String wednesday = wednesdayField.getText();
        String thursday = thursdayField.getText();
        String friday = fridayField.getText();
        String saturday = saturdayField.getText();

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference reference = db.collection("users").document(uid);

        Map<String, Object> update = new HashMap<>();
        update.put("Availability Sunday", sunday);
        update.put("Availability Monday", monday);
        update.put("Availability Tuesday", tuesday);
        update.put("Availability Wednesday", wednesday);
        update.put("Availability Thursday", thursday);
        update.put("Availability Friday", friday);
        update.put("Availability Saturday", saturday);

        try {
            reference.update(update).get();
            System.out.println("Update Successful.");
            errorLabel.setText("Update Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Update Failed. Try again or contact Admin.");
        }
    }

    // getting the apikey
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

    // this method checks the current and new passwords with the user idtoken
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

        if (idToken == null) {
            errorLabel.setText("Current password is incorrect.");
            return;
        }
        changePassword(idToken, newPassword);
        errorLabel.setText("Successful change!");
    }

    // this method uses Http connection to change the firebase auth password for users
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
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            System.out.println("Password changed successfully.");
        } else {
            System.out.println("Failed to change password.");
        }
    }

    // this method allows a popup message when clicked contact us
    @FXML private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Need Help?");
        alert.setHeaderText("If You Need Help, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }
}

