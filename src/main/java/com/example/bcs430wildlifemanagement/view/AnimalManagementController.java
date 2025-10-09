package com.example.bcs430wildlifemanagement.view;

import com.example.bcs430wildlifemanagement.model.App;
import com.example.bcs430wildlifemanagement.animals.Animal;
import com.example.bcs430wildlifemanagement.animals.AnimalData;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

import com.google.cloud.Timestamp;

public class AnimalManagementController {
    // nav buttons
    public void HomePageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Home.fxml");
    }

    public void AnimalsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Animals.fxml");
    }

    public void FormsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Forms.fxml");
    }

    public void AdminPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Admin.fxml");
    }

    public void SettingsPageButton(ActionEvent actionEvent) throws IOException {
        App.setRoot("/com/example/bcs430wildlifemanagement/Settings.fxml");
    }

    @FXML
    private TableView <Animal> animalsTable;
    @FXML
    private TableColumn <Animal, String> nameColumn;
    @FXML
    private TextField speciesField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField ageField;
    @FXML
    private DatePicker birthdatePicker;
    @FXML
    private TextField statusField;
    @FXML
    private TextArea personalityArea;
    @FXML
    private TextArea foodScheduleArea;
    @FXML
    private TextArea dietaryNeedsArea;
    @FXML
    private TextField hospitalizedField;
    @FXML
    private TextArea medicalHistoryArea;
    @FXML
    private TextArea notesArea;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    private final AnimalData data = AnimalData.getInstance();

    @FXML
    public void initialize() {
        if (animalsTable != null) animalsTable.setItems(data.getAnimals());
        if (nameColumn != null) nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        if (animalsTable != null) {
            animalsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
                if (newVal != null) fillForm(newVal);
            });
        }
    }

    private void fillForm(Animal animal) {
        speciesField.setText(animal.getSpecies());
        nameField.setText(animal.getName());
        ageField.setText(String.valueOf(animal.getAge()));
        // force the date to show as the same format
        birthdatePicker.setValue(animal.getBirthdate() == null ? null : localDate(animal.getBirthdate()));
        statusField.setText(animal.getStatus());
        personalityArea.setText(animal.getPersonality());
        foodScheduleArea.setText(animal.getFoodSchedule());
        dietaryNeedsArea.setText(animal.getDietaryNeeds());
        hospitalizedField.setText(String.valueOf(animal.getHospitalized()));
        medicalHistoryArea.setText(animal.getMedicalHistory());
        notesArea.setText(animal.getNotes());
    }
    private Animal readForm(Animal animal) {
        animal.setSpecies(speciesField.getText());
        animal.setName(nameField.getText());
        animal.setAge(parseInt(ageField.getText())); // parse int
        animal.setBirthdate(birthdatePicker.getValue() == null ? null : parseTimestamp(birthdatePicker.getValue()));
        animal.setStatus(statusField.getText());
        animal.setPersonality(personalityArea.getText());
        animal.setFoodSchedule(foodScheduleArea.getText());
        animal.setDietaryNeeds(dietaryNeedsArea.getText());
        animal.setHospitalized(hospitalizedField.getText().charAt(0));
        animal.setMedicalHistory(medicalHistoryArea.getText());
        animal.setNotes(notesArea.getText());
        animal.setImage(""); // placeholder for now since i gotta figure out how to upload images
        return animal;
    }

    // need parseint to make sure age is a number
    private int parseInt(String s) {
        try{
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    // need to parse timestamp
    private Timestamp parseTimestamp(java.time.LocalDate date) {
        return Timestamp.of(java.util.Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    // parse local date
    private java.time.LocalDate localDate(Timestamp time) {
        return time.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // add animal button
    @FXML
    private void addAnimal() {
        try{
            Animal animal = readForm(new Animal());
            data.addAnimal(animal);
            if (animalsTable != null) animalsTable.getSelectionModel().select(animal);
            info("Added animal");
        } catch (Exception e) {
            error("Failed to add animal" + e.getMessage());
        }
    }
    // update animal button
    @FXML
    private void updateAnimal() {
        Animal selected = animalsTable == null ? null : animalsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error ("select an animal ");
            return;
        }
        try {
            readForm(selected);
            data.updateAnimal(selected);
            info("Updated animal");
        } catch (Exception e) {
            error("Failed to update animal " + e.getMessage());
        }
    }
    // placeholder for images for now
    @FXML
    private void uploadPicture() {
        info("work in progress");
    }

    // delete animal button
    @FXML
    private void deleteAnimal() {
        Animal selected = animalsTable == null ? null : animalsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            error("select an animal");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "delete " + selected.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to delete " + selected.getName() + "?");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        try {
            data.deleteAnimal(selected.getId());
            info("Deleted animal");
        } catch (Exception e) {
            error("Failed ");
        }
    }
    // clear the form UI button
    @FXML
    private void clearAnimal() {
        if (animalsTable != null) animalsTable.getSelectionModel().clearSelection();
        speciesField.clear();
        nameField.clear();
        ageField.clear();
        statusField.clear();
        personalityArea.clear();
        foodScheduleArea.clear();
        dietaryNeedsArea.clear();
        hospitalizedField.clear();
        medicalHistoryArea.clear();
        notesArea.clear();
        birthdatePicker.setValue(null);
    }

    // error and info popups
    private void info(String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }
    private void error(String message) {
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

}
