module com.example.bcs430wildlifemanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.jsobject;
    requires java.xml;
    requires java.logging;
    requires javafx.web;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires firebase.admin;
    requires com.google.api.apicommon;

    requires google.cloud.core;
    requires com.google.auth;
    requires java.desktop;
    requires org.apache.httpcomponents.httpclient;
    requires com.google.gson;
    requires java.net.http;
    requires com.google.api.services.storage;
    requires com.google.api.client;

    opens com.example.bcs430wildlifemanagement.inventory to javafx.fxml;
    exports com.example.bcs430wildlifemanagement.inventory;
    opens com.example.bcs430wildlifemanagement to javafx.fxml;
    exports com.example.bcs430wildlifemanagement.view;
    opens com.example.bcs430wildlifemanagement.view to javafx.fxml;
    exports com.example.bcs430wildlifemanagement.model;
    opens com.example.bcs430wildlifemanagement.model to javafx.fxml;

}