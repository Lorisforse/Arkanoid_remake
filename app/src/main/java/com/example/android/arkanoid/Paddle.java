package com.example.android.arkanoid;

import android.hardware.Sensor;

public class Paddle {

    private float x;
    private float y;
    private double velocityX;
    private static final double MAX_SPEED = Sensor.TYPE_ACCELEROMETER_UNCALIBRATED;

    public Paddle(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void update(Joystick joystick){
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        x += velocityX;
    }
}
