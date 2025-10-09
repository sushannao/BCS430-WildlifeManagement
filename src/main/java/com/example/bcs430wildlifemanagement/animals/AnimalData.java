package com.example.bcs430wildlifemanagement.animals;

import com.example.bcs430wildlifemanagement.model.FirestoreContext;
import com.google.cloud.firestore.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class AnimalData {

    private static final AnimalData INSTANCE = new AnimalData();
    public static AnimalData getInstance() {
        return INSTANCE;
    }
    private final ObservableList <Animal> animals = FXCollections.observableArrayList();
    private ListenerRegistration reg;

    private AnimalData() {
        listen();
    }
    public ObservableList<Animal> getAnimals() {
        return animals;
    }
    private void listen() {
        Firestore db = FirestoreContext.firebase();
        if (reg != null) reg.remove();
        reg = db.collection("animals").orderBy("name", Query.Direction.ASCENDING).addSnapshotListener((snap, error) -> {
            if (error != null || snap == null) return;
            List <Animal> fresh = new ArrayList<>(); // list  animals from the database
            for (DocumentSnapshot doc : snap.getDocuments()) {
                Animal animal = Animal.from(doc);
                if (animal != null) fresh.add(animal);
            }
            Platform.runLater(() -> animals.setAll(fresh));
        });
    }
    public void addAnimal(Animal animal) throws Exception {
        Firestore db = FirestoreContext.firebase();
        DocumentReference reference = db.collection("animals").document();
        animal.setId(reference.getId());
        reference.set(animal.toMap()).get();
    }
    public void updateAnimal (Animal animal) throws Exception {
        if (animal.getId()==null || animal.getId().isEmpty()) throw new IllegalArgumentException("No ID");
        Firestore db = FirestoreContext.firebase();
        db.collection("animals").document(animal.getId()).set(animal.toMap()).get();
    }
    public void deleteAnimal(String id) throws Exception {
        Firestore db = FirestoreContext.firebase();
        db.collection("animals").document(id).delete().get();
    }

}
