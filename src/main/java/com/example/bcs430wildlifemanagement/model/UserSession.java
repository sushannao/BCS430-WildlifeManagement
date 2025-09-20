package com.example.bcs430wildlifemanagement.model;

public class UserSession {
    private static String email;

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
