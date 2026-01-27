package com.example.demogame.models;

public class Bet {
    private int carId;
    private double amount;

    public Bet(int carId, double amount) {
        this.carId = carId;
        this.amount = amount;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
