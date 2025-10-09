package com.example.bcs430wildlifemanagement.animals;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Animal {
    public enum Status {
        Hospitalized, Healthy, Sick
    }
    // declare fields
    private String id;
    private String species;
    private String name;
    private int age;
    private Timestamp birthdate;
    private String status;
    private String personality;
    private String foodSchedule;
    private String dietaryNeeds;
    private char hospitalized; // using y or n
    private String medicalHistory;
    private String notes;
    private String image; // animal image

    public Animal() {

    }

    public static Animal from(DocumentSnapshot doc) {
        if (doc == null || !doc.exists()) return null;
        Animal animal = new Animal();
        animal.id = doc.getId();
        animal.species = Objects.toString(doc.get("species"), "");
        animal.name = Objects.toString(doc.get("name"), "");
        animal.age = doc.getLong("age") == null ? 0 : doc.getLong("age").intValue();
        animal.birthdate = doc.getTimestamp("birthdate");
        animal.status = Objects.toString(doc.get("status"), "");
        animal.personality = Objects.toString(doc.get("personality"), "");
        animal.foodSchedule = Objects.toString(doc.get("foodSchedule"), "");
        animal.dietaryNeeds = Objects.toString(doc.get("dietaryNeeds"), "");
        String h = Objects.toString(doc.get("hospitalized"), "");
        animal.hospitalized = h.isEmpty() ? 'N' : h.charAt(0);
        animal.medicalHistory = Objects.toString(doc.get("medicalHistory"), "");
        animal.notes = Objects.toString(doc.get("notes"), "");
        animal.image = Objects.toString(doc.get("image"), "");
        return animal;
    }
    public Map <String, Object> toMap() {
        Map<String, Object> map = new HashMap<> ();
        map.put("species", species);
        map.put("name", name);
        map.put("age", age);
        map.put("birthdate", birthdate);
        map.put("status", status);
        map.put("personality", personality);
        map.put("foodSchedule", foodSchedule);
        map.put("dietaryNeeds", dietaryNeeds);
        map.put("hospitalized", String.valueOf(hospitalized));
        map.put("medicalHistory", medicalHistory);
        map.put("notes", notes);
        map.put("image", image);
        return map;
    }

    // getters and setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Timestamp getBirthdate() {
        return birthdate;
    }
    public void setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPersonality() {
        return personality;
    }
    public void setPersonality(String personality) {
        this.personality = personality;
    }
    public String getFoodSchedule() {
        return foodSchedule;
    }
    public void setFoodSchedule(String foodSchedule) {
        this.foodSchedule = foodSchedule;
    }
    public String getDietaryNeeds() {
        return dietaryNeeds;
    }
    public void setDietaryNeeds(String dietaryNeeds) {
        this.dietaryNeeds = dietaryNeeds;
    }
    public char getHospitalized() {
        return hospitalized;
    }
    public void setHospitalized(char hospitalized) {
        this.hospitalized = hospitalized;
    }
    public String getMedicalHistory() {
        return medicalHistory;
    }
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


}
