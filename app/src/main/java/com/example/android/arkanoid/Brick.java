package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

public class Brick extends View {

    private Bitmap brick;
    private float x;
    private float y;
    private int color;
    private int lives;

    public Brick(Context context, float x, float y, int lives) {
        super(context);
        this.x = x;
        this.y = y;
        this.lives = lives;
        skin();
    }
    public Brick(Context context) {
        super(context);
    }



    public void changeSkin(){
        color = (int) (Math.random() * 9);
        switch (color) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_00_cracked);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_01_cracked);
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_02_cracked);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_03_cracked);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_04_cracked);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_05_cracked);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_06_cracked);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_07_cracked);
                break;
            case 8:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_08_cracked);
                break;
        }

    }

    //assigns a random image to the brick
    public void skin() {
        color = (int) (Math.random() * 9);
        switch (color) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_00);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_01);
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_02);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_03);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_04);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_05);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_06);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_07);
                break;
            case 8:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_08);
                break;
        }
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

    public int getLives(){
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public Bitmap getBrick() {
        return brick;
    }
}
