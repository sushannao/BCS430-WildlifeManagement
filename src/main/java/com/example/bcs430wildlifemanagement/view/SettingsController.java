package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.view.RegisterController;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.text.html.ImageView;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;


public class SettingsController {
    @FXML private TextField emailField;
    @FXML private TextField phoneNumField;
    @FXML private TextField skillsField;
    @FXML private TextField limitsField;
    @FXML private Label errorLabel;

    @FXML private TextField currentPasswordField;
    @FXML private TextField newPasswordField;
    @FXML private TextField confirmNewPasswordField;

    private String uid;


    public void logoutButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/files/Login.fxml");
    }

    public void updateButton(ActionEvent actionEvent) throws IOException, FirebaseAuthException {
        String newEmail = emailField.getText();
        String newPhoneNum = phoneNumField.getText();
        String newSkills = skillsField.getText();
        String newLimits = limitsField.getText();

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("users").document(uid);

        Map<String, Object> update = new HashMap<>();
        update.put("email", newEmail);
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


        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid);


        UserRecord userRecord = FirebaseAuth.getInstance().updateUser(request);
        System.out.println("Successfully updated user: " + userRecord.getUid());

    }
    @FXML private void contactAdminPopUp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Your profile not updating?");
        alert.setHeaderText("Try Again! If it still doesn't work, Contact Admin.");
        alert.setContentText("Name: Admin Suzie \nPhone Number: 123.456.7890 \nEmail: admin@gmail.com");
        alert.showAndWait();
    }



    /* // TODO: changing passwords:
    String password = currentPasswordField.getText();
    String newPassword = newPasswordField.getText();
    String confirmNewPassword = confirmNewPasswordField.getText();


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
    private static final String apiKey = getApiKey(); */

}
