package com.example.android.arkanoid;

public class Ball {

    private int difficulty;
    protected float xSpeed;
    protected float ySpeed;
    private float x;
    private float y;

    public Ball(float x, float y, int difficulty) {
        this.x = x;
        this.y = y;
        this.difficulty = difficulty;
        createSpeed();
    }


    //creates a random ball speed
    protected void createSpeed() {
        int maxX = (int) Math.round(13 * ((double) difficulty / 2 + 0.3));
        int minX = (int) Math.round(7 * ((double) difficulty / 2 + 0.3));
        int maxY = (int) Math.round(-17 * ((double) difficulty / 2 + 0.3));
        int minY = (int) Math.round(-23 * ((double) difficulty / 2 + 0.3));
        int rangeX = maxX - minX + 1;
        int rangeY = maxY - minY + 1;

        xSpeed = (int)  (Math.random()*rangeX) + minX;
        ySpeed = (int)  (Math.random()*rangeY) + minY;
    }

    // changes direction according to speed
    protected void changeDirection() {
        if (xSpeed > 0 && ySpeed < 0) {
            turnXSpeed();
        } else if (xSpeed < 0 && ySpeed < 0) {
            turnYSpeed();
        } else if (xSpeed < 0 && ySpeed > 0) {
            turnXSpeed();
        } else if (xSpeed > 0 && ySpeed > 0) {
            turnYSpeed();
        }
    }

    // increase speed based on level
    protected void increaseSpeed(int level) {
        xSpeed = (float) (xSpeed + 0.5 * level);
        ySpeed = (float) (ySpeed - 0.5 * level);
    }

    //changes direction depending on which wall it touched and speed
    protected void changeDirection(String wall) {
        if (xSpeed > 0 && ySpeed < 0 && wall.equals("right")) {
            turnXSpeed();
        } else if (xSpeed > 0 && ySpeed < 0 && wall.equals("up")) {
            turnYSpeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("up")) {
            turnYSpeed();
        } else if (xSpeed < 0 && ySpeed < 0 && wall.equals("left")) {
            turnXSpeed();
        } else if (xSpeed < 0 && ySpeed > 0 && wall.equals("left")) {
            turnXSpeed();
        } else if (xSpeed > 0 && ySpeed > 0 && wall.equals("down")) {
            turnYSpeed();
        } else if (xSpeed > 0 && ySpeed > 0 && wall.equals("right")) {
            turnXSpeed();
        }
    }

    // find out if the ball is close
    private boolean isClose(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        if ((Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow(ay - by, 2))) < 80) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 100) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        } else if ((Math.sqrt(Math.pow((ax + 150) - bx, 2) + Math.pow(ay - by, 2))) < 60) {
            return true;
        }
        return false;
    }

    // find out if the ball is close to a brick
    private boolean isNearToBrick(float ax, float ay, float bx, float by) {
        bx += 12;
        by += 11;
        double d = Math.sqrt(Math.pow((ax + 50) - bx, 2) + Math.pow((ay + 40) - by, 2));
        return d < 80;
    }

    // if the ball collided with the fall, it will change direction
    protected void SuddenlyPaddle(float xPaddle, float yPaddle) {
        if (isClose(xPaddle, yPaddle, getX(), getY())) changeDirection();
    }

    // if the ball collided with a brick, it will change direction
    protected boolean SuddenlyBrick(float xBrick, float yBrick) {
        if (isNearToBrick(xBrick, yBrick, getX(), getY())) {
            changeDirection();
            return true;
        } else return false;
    }

    // moves by the specified speed
    protected void hurryUp() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void turnXSpeed() {
        xSpeed = -xSpeed;
    }

    public void turnYSpeed() {
        ySpeed = -ySpeed;
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

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }
}
