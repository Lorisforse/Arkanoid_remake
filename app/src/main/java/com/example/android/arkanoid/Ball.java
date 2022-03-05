package com.example.android.arkanoid;

public class Ball {

    private int difficulty;
    protected float xSpeed;
    protected float ySpeed;
    private float x;
    private float y;
    private int minX;
    private int minY;

    public Ball(float x, float y, int difficulty) {
        this.x = x;
        this.y = y;
        this.difficulty = difficulty;
        minX = 7;
        minY = -23;
        createSpeed();
    }


    //creates a random ball speed
    protected void createSpeed() {
        xSpeed =  difficulty == 1 ? minX : difficulty == 2 ? 10 : 13;
        ySpeed =  difficulty == 1 ? minY : difficulty == 2 ? -20 : -17;
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

    //fa rimbalzare la pallina dal paddle
    protected void goUpFromPaddle(){
        if(ySpeed>0){
            turnYSpeed();
        }
    }

    // if the ball collided with the fall, it will change direction
    protected void SuddenlyPaddle(float xPaddle, float yPaddle) {
        if (isClose(xPaddle, yPaddle, getX(), getY())) goUpFromPaddle();
    }

    // if the ball collided with a brick, it will change direction
    protected boolean SuddenlyBrick(float xBrick, float yBrick) {
        if (isNearToBrick(xBrick, yBrick, getX(), getY())) {
            return true;
        } else return false;
    }

    // moves by the specified speed
    protected void hurryUp() {
        x = x + xSpeed;
        y = y + ySpeed;
    }

    // slow down the speed
    protected void slowDown(){
        if(xSpeed > 0){
            xSpeed = xSpeed - 3;
        }else{
            xSpeed = xSpeed + 3;
        }
        if(ySpeed < 0) {
            ySpeed = ySpeed + 3;
        }else {
            ySpeed = ySpeed - 3;
        }
    }

    //speed up the speed
    protected void speedUp(){
        if(xSpeed > 0){
            xSpeed = xSpeed + 3;
        }else{
            xSpeed = xSpeed - 3;
        }
        if(ySpeed < 0) {
            ySpeed = ySpeed - 3;
        }else {
            ySpeed = ySpeed + 3;
        }
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
