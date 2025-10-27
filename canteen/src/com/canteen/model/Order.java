package com.canteen.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private Integer token; 
    private String itemName;
    private int quantity;
    private float price;
    private LocalDateTime orderTime;
    private boolean isComplete;

    // Constructor for NEW orders
    public Order(String itemName, int quantity, float price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.orderTime = LocalDateTime.now();
        this.isComplete = false;
    }
    
    // Constructor for REHYDRATING from the database
    public Order(int token, String itemName, int quantity, float price, LocalDateTime orderTime, boolean isComplete) {
        this.token = token;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.orderTime = orderTime;
        this.isComplete = isComplete;
    }

    // Getters
    public Integer getToken() { return token; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public float getPrice() { return price; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public boolean isComplete() { return isComplete; }
    public float getTotalPrice() { return quantity * price; }

    public String getFormattedOrderTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return orderTime.format(formatter);
    }

    // Setters
    public void setToken(int token) { this.token = token; }
    public void setComplete(boolean complete) { isComplete = complete; }
}