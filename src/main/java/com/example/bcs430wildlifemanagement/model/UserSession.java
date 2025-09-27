package com.example.bcs430wildlifemanagement.model;

import com.google.firebase.auth.FirebaseAuth;

public class UserSession {
    private static String email;
    private static String uid;

    public static void setUid(String userUid) {
        uid = userUid;
    }

    public static String getUid() {
        return uid;
    }

    public static void setEmail(String userEmail) {
        email = userEmail;
    }

    public static String getEmail() {
        return email;
    }

    public static String getUsername() {
        if (email == null) return "";
        String username = email.split("@")[0];
        return username.substring(0, 1).toUpperCase() + username.substring(1);
    }
}
