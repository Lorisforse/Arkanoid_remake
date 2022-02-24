package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.util.Random;

public class PowerUp extends View {
    private Bitmap powerUp;
    private float x;
    private float y;
    protected float ySpeed;
    private int effect;
    private boolean active = false;
    private int a;

    public PowerUp(Context context,float x, float y){
        super(context);
        this.x = x;
        this.y = y;
        effect = effect();
        setSpeed();
        a = 0;
    }

    private int effect(){
        Random c = new Random();
        a = c.nextInt(4);
        switch (a) {
            case 0:
                //one more life
                powerUp = BitmapFactory.decodeResource(getResources(), R.drawable.lifeup);
                break;
            case 1:
                //convert the ball into a fire ball that destroy bricks widout bouncing on them (for n bricks)
                powerUp = BitmapFactory.decodeResource(getResources(), R.drawable.destroy_brick);
                break;
            case 2:
                // make the paddle bigger
                powerUp = BitmapFactory.decodeResource(getResources(), R.drawable.expand);
                break;
            case 3:
                //slow down the speed
                powerUp = BitmapFactory.decodeResource(getResources(), R.drawable.slowdown);
                break;
        }
        return a;
    }

    // set the speed at which the power up falls
    public void setSpeed(){
        ySpeed = -10;
    }

    protected boolean isNear (float xPaddle, float yPaddle) {
        float x = getX()+12;
        float y = getY()+11;
        if ((Math.sqrt(Math.pow((xPaddle + 50) - x, 2) + Math.pow(yPaddle - y, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((xPaddle + 100) - x, 2) + Math.pow(yPaddle - y, 2))) < 60) {
            return true;
        } else if ((Math.sqrt(Math.pow((xPaddle + 150) - x, 2) + Math.pow(yPaddle - y, 2))) < 60) {
            return true;
        }
        return false;
    }

    //moves the power up towards the bottom
    protected void hurryUp() {
        active = true;
        y = y - ySpeed;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public boolean getActive(){ return active;}

    public int getEffect() { return effect; }

    public Bitmap getPowerUp() {
        return powerUp;
    }

}
