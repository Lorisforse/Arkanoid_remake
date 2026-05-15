package com.example.android.arkanoid.javaClass_activity;

public class Paddle {

    private float x;
    private float y;
    private double velocityX;
    private static final double MAX_SPEED = 15.0;

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
