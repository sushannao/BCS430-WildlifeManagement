package com.example.bcs430wildlifemanagement.inventory;

import com.example.bcs430wildlifemanagement.model.FirestoreContext;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.ListenerRegistration;
import com.google.cloud.firestore.Query;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestsData {
    private static final RequestsData INSTANCE = new RequestsData();
    public static RequestsData getInstance() {
        return INSTANCE;
    }
    private final ObservableList<ResupplyRequest> requests = FXCollections.observableArrayList();
    private ListenerRegistration reg;
    public ObservableList<ResupplyRequest> getRequests() {
        return requests;
    }
    private RequestsData() {
        listen();
    }
    // listens for changes
    private void listen() {
        try {
            Firestore db = FirestoreContext.firebase();
            reg = db.collection("resupplyRequests").addSnapshotListener((snap, error) -> {
                if (error != null || snap == null) return;
                List <ResupplyRequest> fresh = new ArrayList<>();
                for (DocumentSnapshot doc : snap.getDocuments()) {
                    try{
                        Timestamp time = doc.getTimestamp("created");
                        if (time == null) time = doc.getTimestamp("Timestamp: ");
                        // request data gets built
                        fresh.add(new ResupplyRequest(doc.getId(), Objects.toString(doc.get("itemId"), ""), Objects.toString(doc.get("itemName"), ""), toInt(doc.get("quantityRequested"), 0), Objects.toString(doc.get("unit"), ""), Objects.toString(doc.get("notes"), ""), Objects.toString(doc.get("requesterName"), ""), Objects.toString(doc.get("requestedEmail"), ""), time));
                    } catch (Exception ignore) {
                    }
                }
                Platform.runLater(() -> requests.setAll(fresh)); // update list
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // lets admin fulfill a resupply request
    public void fulfillRequest(String requestId, String itemId, int qtyToAdd) throws Exception {
        var db = FirestoreContext.firebase();
        var reqRef = db.collection("resupplyRequests").document(requestId); // going to keep this for status updates (complete, pending, denied)
        if (itemId != null && !itemId.isBlank()) {
            var invRef = db.collection("inventory").document(itemId);
            db.batch().update(invRef, "quantity", FieldValue.increment(qtyToAdd)).commit().get();
        }

    }
    // converts an object to an integer
    private int toInt(Object o, int def) {
        try {
            if (o == null) return def;
            if (o instanceof Number n) return n.intValue();
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return def;
        }
    }
}
