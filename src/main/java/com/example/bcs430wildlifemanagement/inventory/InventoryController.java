package com.example.bcs430wildlifemanagement.inventory;


import javafx.fxml.FXML;

public class InventoryController {
    private final InventoryData data = InventoryData.getInstance();


    @FXML
    public void initialize() {
        // will populate this when creating the fxml
    }

    public void invShowInventory() {
        System.out.println("Inventory:");
        data.getItems().forEach(item -> System.out.println(item.getName() + " " + item.getQuantity() + " " + item.getUnit()));
    }
    public void invRequest(String itemName) {
        InventoryItem item = findByName(itemName);
        if (item == null) {
            System.out.println("Incorrect name");
            return;
        }
        data.requestRestock(item);
        System.out.println("Requested " + item.getName());
    }
    public void invViewRequests() {
        if (data.getRequests().isEmpty()) {
            System.out.println("No requests");
        } else {
            String list = data.getRequests().stream().map(InventoryItem::getName).reduce((a, b) -> a + ", " + b).orElse("");
            System.out.println("Requests: " + list);
        }
    }
    private InventoryItem findByName(String name) {
        return data.getItems().stream().filter(item -> item.getName().equals(name)).findFirst().orElse(null);
    }

    // delete this, just for testing
    public static void main(String[] args) {
        InventoryController controller = new InventoryController();
        controller.invRequest("Bear Food");
        controller.invViewRequests();
        controller.invRequest("First Aid");
        controller.invViewRequests();
        controller.invRequest("Disinfectant Wipes");
        controller.invRequest("Gloves");
        controller.invViewRequests();
        controller.invRequest("1231231");

        System.out.println("-------------------------");
        controller.invShowInventory();
    }
}
