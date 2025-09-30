package com.example.bcs430wildlifemanagement.model;

import com.google.firebase.auth.FirebaseAuth;

public class UserSession {
    private static String email;
    private static String uid;
    private static String idToken;

    // these are setters and getters for user session information
    public static void setUid(String userUid) {
        uid = userUid;
    }
    public static String getUid() {
        return uid;
    }

    public static void setIdToken(String userToken) {
        idToken = userToken;
    }
    public static String getIdToken() {
        return idToken;
    }

    public static void setEmail(String userEmail) {
        email = userEmail;
    }
    public static String getEmail() {
        return email;
    }

    // this is to display name on the home page
    public static String getUsername() {
        if (email == null) return "";
        String username = email.split("@")[0];
        return username.substring(0, 1).toUpperCase() + username.substring(1);
    }
}
