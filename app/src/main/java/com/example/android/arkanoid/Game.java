package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Game extends View implements SensorEventListener, View.OnTouchListener {

    private Joystick joystick;
    private Bitmap background;
    private Bitmap redBall;
    private Bitmap stretchedOut;
    private Bitmap paddle_p;

    private Display display;
    private Point size;
    private Paint paint;

    private Ball ball;
    private ArrayList<Brick> list;
    private Paddle paddle;

    private RectF r;

    private SensorManager sManager;
    private Sensor accelerometer;

    private String record;

    private int lifes;
    private int score;
    private int level;
    private boolean start;
    private boolean gameOver;
    private Context context;
    private int gameMode;
    /*
    0= joystick abilitato
    1= accellerometro abilitato
    2= touchscreen abilitato
    */
    public Game(Context context, int lifes, int score, int gameMode) {
        super(context);
        paint = new Paint();

        // set context, lives, scores and levels
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.gameMode = gameMode;
        level = 0;

        //start a gameOver to find out if the game is standing and if the player has lost
        start = false;
        gameOver = false;

        //creates an accelerometer and a SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        readBackground(context);

        // create a bitmap for the ball and paddle
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //creates a new ball, paddle, and list of bricks
        ball = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2, size.y - 400);
        list = new ArrayList<Brick>();

        //create a new Joystick
        joystick = new Joystick(size.x / 2, size.y - 200, 70,  40);//


        generateBricks(context);
        this.setOnTouchListener(this);

    }

    // fill the list with bricks
    private void generateBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                list.add(new Brick(context, j * 150, i * 100));
            }
        }
    }

    //set background
    private void readBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background_score));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    protected void onDraw(Canvas canvas) {
        // creates a background only once
        if (stretchedOut == null) {
            stretchedOut = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(stretchedOut, 0, 0, paint);

        //draw the ball
        paint.setColor(Color.RED);
        canvas.drawBitmap(redBall, ball.getX(), ball.getY(), paint);

        // draw paddle
        paint.setColor(Color.WHITE);
        r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + 200, paddle.getY() + 40);
        canvas.drawBitmap(paddle_p, null, r, paint);

        //draw bricks
        paint.setColor(Color.GREEN);
        for (int i = 0; i < list.size(); i++) {
            Brick b = list.get(i);
            r = new RectF(b.getX(), b.getY(), b.getX() + 100, b.getY() + 80);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // draw text
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("" + lifes, 400, 100, paint);
        canvas.drawText("" + score, 700, 100, paint);

        //in case of loss draw "Game over!"
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", size.x / 4, size.y / 2, paint);
        }
    }

    // check that the ball has not touched the edge
    private void checkEdges() {
        if (ball.getX() + ball.getxSpeed() >= size.x - 60) {
            ball.changeDirection("right");
        } else if (ball.getX() + ball.getxSpeed() <= 0) {
            ball.changeDirection("left");
        } else if (ball.getY() + ball.getySpeed() <= 150) {
            ball.changeDirection("up");
        } else if (ball.getY() + ball.getySpeed() >= size.y - 200) {
            checkLives();
        }
    }

    //checks the status of the game. whether my lives or whether the game is over
    private void checkLives() {
        if (lifes == 1) {
            //Save record in a String
            record=(score+", ");
            saveRecord();
            gameOver = true;
            start = false;
            invalidate();
        } else {
            lifes--;
            ball.setX(size.x / 2);
            ball.setY(size.y - 480);
            ball.createSpeed();
            ball.increaseSpeed(level);
            start = false;
        }
    }

    //each step checks whether there is a collision, a loss or a win, etc.
    public void update() {
        if(gameMode ==1){
            joystick.update();//
            paddle.update(joystick);//
        }
        if (start) {
            //check if the paddle touch the edges
            if(paddle.getX() <= 0)
                paddle.setX(0);

            if(paddle.getX()>= 870)
                paddle.setX(870);

            win();
            checkEdges();
            ball.SuddenlyPaddle(paddle.getX(), paddle.getY());
            for (int i = 0; i < list.size(); i++) {
                Brick b = list.get(i);
                if (ball.SuddenlyBrick(b.getX(), b.getY())) {
                    list.remove(i);
                    score = score + 80;
                }
            }
            ball.hurryUp();
        }
    }

    public void stopSensing() {
        if(gameMode ==0)
            sManager.unregisterListener(this);
    }

    public void runScanning() {
        if(gameMode ==0)
            sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    //change accelerometer
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(gameMode==0){
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                paddle.setX(paddle.getX() - event.values[0] - event.values[0]);
                if (paddle.getX() + event.values[0] > size.x - 240) {
                    paddle.setX(size.x - 240);
                } else if (paddle.getX() - event.values[0] <= 20) {
                    paddle.setX(20);
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //serves to suspend the game in case of a new game
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver == true && start == false) {
            score = 0;
            lifes = 3;
            resetLevel();
            gameOver = false;

        } else {
            start = true;
        }
        return false;
    }

    // sets the game to start
    private void resetLevel() {
        ball.setX(size.x / 2);
        ball.setY(size.y - 480);
        ball.createSpeed();
        list = new ArrayList<Brick>();
        generateBricks(context);
    }

    // find out if the player won or not
    private void win() {
        if (list.isEmpty()) {
            ++level;
            resetLevel();
            ball.increaseSpeed(level);
            start = false;
        }
    }

    //Save the high score
    public void saveRecord() {

        try
        {
            FileOutputStream fOut = context.openFileOutput("record.txt", Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(record);
            osw.flush();
            osw.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    @Override //draw the joystick
    public void draw(Canvas canvas){
        if(gameMode ==1){
            super.draw(canvas);
            joystick.draw(canvas);
        }else if(gameMode !=1){
            super.draw(canvas);
            joystick.draw(canvas);
            joystick.setOuterCircleRadius(0);
            joystick.setInnerCircleRadius(0);
        }


    }//

    @Override //event for joystick
    public boolean onTouchEvent(MotionEvent event) {
		if(gameMode==2){
            switch(event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    paddle.setX(event.getX());
                    if (paddle.getX() > size.x - 240) {
                        paddle.setX(size.x - 235);
                    } else if (paddle.getX() < 20) {
                        paddle.setX(20);
                    }
            }
            return true;
        }
        if(gameMode ==1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (joystick.isPressed((double) event.getX())) {
                        joystick.setIsPressed(true);
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (joystick.getIsPressed()) {
                        joystick.setActuator((double) event.getX());
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
