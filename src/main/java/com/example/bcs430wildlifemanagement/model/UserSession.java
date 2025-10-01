package com.example.bcs430wildlifemanagement.model;

import com.google.firebase.auth.FirebaseAuth;

public class UserSession {
    private static String email;
    private static String uid;
    private static String idToken;
    private static String fName;

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

    public static void setFName(String username) { fName = username; }

    public static String getFName() { return fName; }
}
