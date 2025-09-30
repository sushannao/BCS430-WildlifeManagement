package com.example.bcs430wildlifemanagement.inventory;

import com.google.cloud.Timestamp;
import java.util.Objects;

public class ResupplyRequest {
    // request fields
    private String id;
    private String itemId;
    private String itemName;
    private String notes;
    private String requestedEmail;
    private String requesterName;
    private String unit;
    private Timestamp created;
    private int quantityRequested;

    public ResupplyRequest() {

    }
    // constructor
    public ResupplyRequest(String id, String itemId, String itemName, int quantityRequested, String unit, String notes, String requesterName, String requestedEmail, Timestamp created) {
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantityRequested = quantityRequested;
        this.unit = unit;
        this.notes = notes;
        this.requesterName = requesterName;
        this.requestedEmail = requestedEmail;
        this.created = created;
    }
    // getters
    public String getId() {
        return id;
    }
    public String getItemId() {
        return itemId;
    }
    public String getItemName() {
        return itemName;
    }
    public int getQuantityRequested() {
        return quantityRequested;
    }
    public String getUnit() {
        return unit;
    }
    public String getNotes() {
        return notes;
    }
    public String getRequesterName() {
        return requesterName;
    }
    public String getRequestedEmail() {
        return requestedEmail;
    }
    public Timestamp getCreated() {
        return created;
    }
    @Override
    public boolean equals (Object o) {
        return this==o || o instanceof ResupplyRequest r && Objects.equals(id, r.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}