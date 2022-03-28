package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;


public class Game extends View implements SensorEventListener {

    private Joystick joystick;
    private Bitmap background;
    private Bitmap redBall;
    private Bitmap stretchedOut;
    private Bitmap paddle_p;

    private Rect quit;
    private Rect resume;
    private Rect loser;

    private Display display;
    private Point size;
    private Paint paint;

    private Ball ball;
    private ArrayList<Brick> list;
    private Paddle paddle;

    private ArrayList<PowerUp> powerUps;

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
    private int difficulty;
    private int grandezza=200;
    private float setpaddle;
    private boolean potenza3;//iceball
    private boolean potenza1;//fireball
    private boolean potenza2;//enlarge paddle
    private SoundPlayer sound;
    int piuGrande;
    int[] brickX;
    int[] brickY;
    String [] arrayX;
    String [] arrayY;

    /*
    0= joystick abilitato
    1= accellerometro abilitato
    2= touchscreen abilitato
    */
    public Game(Context context, int lifes, int score, int gameMode, int difficulty) {
        super(context);
        paint = new Paint();

        brickX= new int[20];
        brickY= new int[20];
        // set context, lives, scores and levels
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        level = 0;
        sound = new SoundPlayer(context);

        //load font
        Typeface myFont = ResourcesCompat.getFont(context,R.font.audiowide);
        paint.setTypeface(myFont);

        //start a gameOver to find out if the game is standing and if the player has lost
        start = false;
        gameOver = false;

        //creates an accelerometer and a SensorManager
        sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        readBackground(context);

        // create a bitmap for the ball and paddle
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.pinkball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //creates a new ball, paddle, and list of bricks
        ball = new Ball(size.x / 2, size.y - 480, difficulty);
        paddle = new Paddle(size.x / 2, size.y - 400);
        list = new ArrayList<Brick>();
        powerUps = new ArrayList<PowerUp>();
        potenza1= false;
        potenza2= false;
        potenza3= false;

        //create a new Joystick
        joystick = new Joystick(size.x / 2, size.y - 200, 70,  40);


        generateBricks(context);
        if(difficulty!=4){
            generatePowerUps(context);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // creates a background only once
        if (stretchedOut == null) {
            stretchedOut = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(stretchedOut, 0, 0, paint);

        //draw the ball
        if(potenza1==true){
            redBall = BitmapFactory.decodeResource(getResources(), R.drawable.fireball);
        }else
            redBall = BitmapFactory.decodeResource(getResources(), R.drawable.pinkball);
        paint.setColor(Color.RED);
        canvas.drawBitmap(redBall, ball.getX(), ball.getY(), paint);

        // draw paddle
        if(potenza2==true)
            piuGrande=100;
        else
            piuGrande=0;
        paint.setColor(Color.WHITE);
        setpaddle=paddle.getX();
        r = new RectF(paddle.getX(), paddle.getY(), setpaddle + grandezza + piuGrande, paddle.getY() + 40);
        canvas.drawBitmap(paddle_p, null, r, paint);

        // draw powerUps
        paint.setColor(Color.YELLOW);
        for( int i = 0 ; i < powerUps.size() ; i++){
            PowerUp p = powerUps.get(i);
            r = new RectF(p.getX(),p.getY(), p.getX()+100, p.getY() +80);
            canvas.drawBitmap(p.getPowerUp(),null,r,paint);
        }

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
            if (gameOver) {
                loser = new Rect(size.x/8,size.y/2-110,size.x*7/8,size.y/2+50);
                quit = new Rect(size.x/4-70,size.y/2+100,size.x/4+210,size.y/2+250);
                resume = new Rect((size.x*3/5)-90,(size.y/2)+100,(size.x*3/4)+100,size.y/2+250);
                paint.setColor(Color.BLACK);
                canvas.drawRect(loser,paint);
                paint.setTextSize(130);
                paint.setColor(Color.RED);
                canvas.drawText("Game Over!",size.x/8,size.y/2,paint);
                paint.setColor(Color.TRANSPARENT);
                canvas.drawRect(quit,paint);
                canvas.drawRect(resume,paint);
                paint.setTextSize(100);
                paint.setColor(Color.WHITE);
                canvas.drawText("Quit!",size.x/4-60,size.y/2+200,paint);
                canvas.drawText("Retry",(size.x*3/5)-70,size.y/2+200,paint);
            }
        }
    }

    // fill the list with bricks
    private void generateBricks(Context context) {
        int maxY = difficulty == 1 ? 6 : difficulty == 2 ? 7 : 8;
        int maxX = difficulty == 1 ? 5 : difficulty == 2 ? 6 : 7;
        readBrick();

        if (difficulty==4) {
            for (int f = 0; f < (brickX.length) - 1; f++) {
                brickX = splitAndConvert(arrayX, arrayX.length);
                brickY = splitAndConvert(arrayY, arrayY.length);
                list.add(new Brick(context, brickX[f], brickY[f], 1));
            }
        }
        for (int i = 3; i < maxY; i++) {
            for (int j = 1; j < maxX; j++) {
                int lives = 0;
                switch (difficulty) {
                    case 1:
                        lives = new Random().nextInt(4);
                        list.add(new Brick(context, j * 200, i * 130, lives == 0 ? 2 : 1));
                        break;
                    case 2:
                        lives = new Random().nextInt(4);
                        list.add(new Brick(context, j * 160, i * 120, lives < 2 ? 2 : 1));
                        break;
                    case 3:
                        lives = new Random().nextInt(10);
                        list.add(new Brick(context, j * 140, i * 110, lives == 0 ? 3 : lives < 5 ? 2 : 1));
                        break;
                }
            }
        }
    }

    //fill the powerUps list
    private  void generatePowerUps(Context context){
        int a = difficulty == 1 ? 3 : difficulty == 2 ? 5 : 7;
        int maxY = difficulty == 1 ? 6 : difficulty == 2 ? 7 : 8;
        int maxX = difficulty == 1 ? 5 : difficulty == 2 ? 6 : 7;
        PowerUp pn = null;
        boolean flag ;
        for(int i = 0; i < a; i++) {
            flag = false;
            while (!flag) {
                if (difficulty == 1) {
                    pn = new PowerUp(context, ((int) (Math.random() * (maxX - 1)) + 1) * 200, ((int) (Math.random() * (maxY - 3)) + 3) * 130);
                } else if (difficulty == 2) {
                    pn = new PowerUp(context, ((int) (Math.random() * (maxX - 1)) + 1) * 160, ((int) (Math.random() * (maxY - 3)) + 3) * 120);
                } else {
                    pn = new PowerUp(context, ((int) (Math.random() * (maxX - 1)) + 1) * 140, ((int) (Math.random() * (maxY - 3)) + 3) * 110);
                }
                flag = checkCoordinates(pn);
                if (flag) {
                    powerUps.add(pn);
                }
            }
        }
    }

    //serve a confrontare le posizioni già generate con le nuove random per evitare che vengano
    //prodotte coordinate duplicate

    private boolean checkCoordinates(PowerUp powerUp){
        boolean flag = true;
        int i=1;
        if(powerUps.isEmpty()){
            flag = true;
        }else{
            for(PowerUp p: powerUps){
                if(powerUp.getX()==p.getX()){
                    if(powerUp.getY()==p.getY()){
                        flag = false;
                    }
                }else{
                    i++;
                }
                if((i==powerUps.size())&&(flag)){
                    flag = true;
                }
            }
        }
        return flag;
    }

    //set background
    private void readBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background_score));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }



    // check that the ball has not touched the edge
    private void checkEdges() {
        if (ball.getX() + ball.getxSpeed() >= size.x - 60) {
            ball.changeDirection("right");
        } else if (ball.getX() + ball.getxSpeed() <= 0) {
            ball.changeDirection("left");
        } else if (ball.getY() + ball.getySpeed() <= 150) {
            potenza1=false;
            potenza2=false;
            potenza3=false;
            ball.changeDirection("up");
        } else if (ball.getY() + ball.getySpeed() >= size.y - 200) {
            potenza1=false;
            potenza2=false;
            if(potenza3){
                ball.speedUp();
                potenza3=false;
            }
            for (int i = 0; i < powerUps.size(); i++)
                if(powerUps.get(i).getActive()){
                    powerUps.remove(i);
                }
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
            if(Constants.getFlag())
                sound.playGameOver();
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
        //check if the paddle touch the edges
        if(paddle.getX() <= 0)
            paddle.setX(0);

        if(paddle.getX()>= 870)
            paddle.setX(870);

        if(gameMode ==1){
            joystick.update();//
            paddle.update(joystick);//
        }
        if (start) {
            win();
            checkEdges();
            ball.SuddenlyPaddle(paddle.getX(), paddle.getY());
            for (int i = 0; i < list.size(); i++) {
                Brick b = list.get(i);
                if (ball.SuddenlyBrick(b.getX(), b.getY())) {
                    if(Constants.getFlag())
                        sound.playBrick();
                    if ((b.getLives() >= 1)&&(!potenza1)) {
                        ball.changeDirection();
                        b.changeSkin();
                        b.setLives(b.getLives() - 1);
                    } else {
                        for(int j = 0; j < powerUps.size(); j ++){
                            PowerUp p = powerUps.get(j);
                            if((p.getY()==b.getY())&&(p.getX()==b.getX())) {
                                p.hurryUp();
                            }
                        }
                        if(!potenza1)
                            ball.changeDirection();
                        list.remove(i);
                        score = difficulty == 1 ? score + 80 : difficulty == 2 ? score + 40 : score + 20;
                    }
                }

            }
            for (int i = 0; i < powerUps.size(); i++){
                PowerUp p = powerUps.get(i);
                if(p.isNear(paddle.getX(),paddle.getY())){
                    switch (p.getEffect()){
                        case 0:
                            if(Constants.getFlag())
                                sound.playLife();
                            lifes++;
                            break;
                        case 1:
                            if(Constants.getFlag())
                                sound.playFire();
                            potenza1 =true;
                            break;
                        case 2:
                            if(Constants.getFlag())
                                sound.playPaddle();
                            potenza2 = true;
                            break;
                        case 3:
                            if(Constants.getFlag())
                                sound.playIce();
                            potenza3 = true;
                            ball.slowDown();
                            break;
                    }
                    powerUps.remove(i);
                }else if (p.getActive()){
                    p.hurryUp();
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



    // sets the game to start
    private void resetLevel() {
        ball.setX(size.x / 2);
        ball.setY(size.y - 480);
        ball.createSpeed();
        list = new ArrayList<Brick>();
        generateBricks(context);
        if(difficulty!=4){
            generatePowerUps(context);
        }
    }

    // find out if the player won or not
    private void win() {
        if (list.isEmpty()) {
            ++level;
            if(Constants.getFlag())
                sound.playWin();
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
        if(gameOver){
            if(quit.contains((int)event.getX(),(int)event.getY())){
                Intent intent = new Intent(context, MainMenu.class);
                context.startActivity(intent);
                start = false;
            }else if(resume.contains((int)event.getX(),(int)event.getY())){
                resetLevel();
            }
        }
        if (gameMode !=1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    gameOver();
                    if (!start || gameOver == true)
                        start = true;
                case MotionEvent.ACTION_MOVE:
                    if (gameMode == 2) {
                        if (start) {

                            paddle.setX(event.getX());
                            if (paddle.getX() > size.x - 210) {
                                paddle.setX(size.x - 210);
                            } else if (paddle.getX() < 20) {
                                paddle.setX(20);
                            }
                        }
                    }
                    return true;
            }
        }
        if(gameMode ==1) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    gameOver();
                    start=true;
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



    public void readBrick(){
        String result = "";
        String result2[];
        try {
            InputStream inputStream = context.openFileInput("brick.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("").append(receiveString);
                }
                result = stringBuilder.toString();
                result2=result.split(":");
                arrayX =(result2[0].split(", "));
                arrayY =(result2[1].split(", "));


                inputStream.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] splitAndConvert(String[] array, int length){
        int [] result= new int[length+1];
        try{

            for(int i = 0; i< length; i++){
                result[i] = Integer.parseInt(String.valueOf(array[i])); //Note charAt
            }
        }catch (Exception e){
            Toast.makeText(context, "ERRORE",Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public void gameOver(){
        if (gameOver == true && start == false) {
            score = 0;
            lifes = 3;
            resetLevel();
            gameOver = false;
        }
    }
}