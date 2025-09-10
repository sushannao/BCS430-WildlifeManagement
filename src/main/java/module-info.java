module com.example.bcs430wildlifemanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bcs430wildlifemanagement to javafx.fxml;
    exports com.example.bcs430wildlifemanagement;
}