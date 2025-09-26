





        return Objects.hash(id);
        return created;
        return id;
        return itemId;
        return itemName;
        return notes;
        return quantityRequested;
        return requestedEmail;
        return requesterName;
        return this==o || o instanceof ResupplyRequest r && Objects.equals(id, r.id);
        return unit;
        this.created = created;
        this.id = id;
        this.itemId = itemId;
        this.itemName = itemName;
        this.notes = notes;
        this.quantityRequested = quantityRequested;
        this.requestedEmail = requestedEmail;
        this.requesterName = requesterName;
        this.unit = unit;
    @Override
    @Override
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
    public ResupplyRequest(String id, String itemId, String itemName, int quantityRequested, String unit, String notes, String requesterName, String requestedEmail, Timestamp created) {
    public String getId() {
    public String getItemId() {
    public String getItemName() {
    public String getNotes() {
    public String getRequestedEmail() {
    public String getRequesterName() {
    public String getUnit() {
    public Timestamp getCreated() {
    public boolean equals(Object o) {
    public int getQuantityRequested() {
    public int hashCode() {
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
    }
import com.google.cloud.Timestamp;
import java.util.Objects;
package com.example.bcs430wildlifemanagement.inventory;
public class ResupplyRequest {
}
