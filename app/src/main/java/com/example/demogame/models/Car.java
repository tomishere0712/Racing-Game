package com.example.demogame.models;

public class Car {
    private int id;
    private String name;
    private int drawableResId;
    private float speed;
    private float position;
    private int finishPosition;

    public Car(int id, String name, int drawableResId) {
        this.id = id;
        this.name = name;
        this.drawableResId = drawableResId;
        this.speed = 0;
        this.position = 0;
        this.finishPosition = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPosition() {
        return position;
    }

    public void setPosition(float position) {
        this.position = position;
    }

    public int getFinishPosition() {
        return finishPosition;
    }

    public void setFinishPosition(int finishPosition) {
        this.finishPosition = finishPosition;
    }
}
