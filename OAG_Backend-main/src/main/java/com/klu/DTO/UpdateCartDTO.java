package com.klu.DTO;

public class UpdateCartDTO {

    private int orderId;
    private int newAmount;

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getNewAmount() {
        return newAmount;
    }
    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }
}