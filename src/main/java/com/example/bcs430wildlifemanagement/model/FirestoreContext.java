package com.example.bcs430wildlifemanagement.model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.io.InputStream;

public class FirestoreContext {

    public static Firestore firebase() {
        try (InputStream input = FirestoreContext.class.getResourceAsStream("/key.json")) {
            if (input ==null) throw new IllegalStateException("Key file not found");
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(input))
                        .build();
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase is initialized");
            }
            return FirestoreClient.getFirestore();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return FirestoreClient.getFirestore();
    }
}

