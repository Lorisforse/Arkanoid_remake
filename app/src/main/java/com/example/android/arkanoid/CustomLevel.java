package com.example.android.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CustomLevel extends View implements GestureDetector.OnGestureListener{

    private Bitmap stretchedOut;
    private Bitmap background;
    private Paint paint;
    private Display display;
    private Point size;

    private int[] x;
    private int[] y;
    private Bitmap[] b_brick;
    private Bitmap paddle;
    private GestureDetector detector;
    //RECT PER I SINGOLI BRICK
    private Rect[] r;
    private Rect[] r2;
    boolean locked[];
    private final static int TOTAL_BRICK =20;
    boolean isPressed[];
    private int j;
    private int k;
    private Brick brick;
    private Bitmap play;
    private Rect plays;
    private String mess;
    boolean paddleLock;
    int paddleX,paddleY;
    private Rect paddleR;
    //COSTRUTTORE
    public CustomLevel (Context context)  {
        super(context);
        paint = new Paint();
        readBackground(context);
        detector=new GestureDetector(context, this);

        paddleX=600;
        paddleY=20;
        paddleLock=true;
        j=-1;
        k=1;
        x = new int[TOTAL_BRICK];
        y = new int[TOTAL_BRICK];
        b_brick = new Bitmap[TOTAL_BRICK];
        r = new Rect[TOTAL_BRICK];
        r2 = new Rect[3];
        locked = new boolean[TOTAL_BRICK];
        isPressed = new boolean[3];
        for(int i=0; i<r2.length;i++)
            r2[i] = new Rect();
        for(int i=0; i<TOTAL_BRICK;i++)
            r[i] = new Rect();
        brick = new Brick(context);
        play = BitmapFactory.decodeResource(getResources(), R.drawable.visto);
        play = Bitmap.createScaledBitmap(play, 200,200,false);
        plays= new Rect();
        paddleR = new Rect();
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);


        for(int i=0; i<TOTAL_BRICK; i++){
            brick.setBreakable(true);
            brick.skin();
            b_brick[i] = brick.getBrick();
            b_brick[i] = Bitmap.createScaledBitmap(b_brick[i],105,85,false);
            locked[i]=true;
        }
        plays.set(410,1900,630,2100);

        mess = getResources().getString(R.string.custom_advice);
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // creates a background only once
        if (stretchedOut == null) {
            stretchedOut = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(stretchedOut, 0, 0, paint);
        r2[0].set(0,10,125,115);
        r2[1].set(590,10,810,80);
        //canvas.drawRect(r2[1],paint);

        if(isPressed[0]){
            paint.setColor(Color.YELLOW);
            canvas.drawRect(r2[0],paint);
        }
        if(isPressed[1]){
            paint.setColor(Color.YELLOW);
            canvas.drawRect(r2[1],paint);
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        canvas.drawText("X "+(TOTAL_BRICK-j-1),130,70,paint);
        canvas.drawText("X "+(k),850,70,paint);


        //DRAW BRICK
        for(int i=0; i<TOTAL_BRICK; i++){
            r[i].set(x[i], y[i],x[i]+200, y[i]+120);
            canvas.drawBitmap(b_brick[i],x[i]+10, y[i]+20, paint);
        }

        canvas.drawBitmap(play, null, plays,paint);
        paddleR.set(paddleX, paddleY,paddleX+200, paddleY+50);
        canvas.drawBitmap(paddle,null,paddleR,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        switch( event.getAction() ){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(!paddleLock && !r2[1].contains((int)event.getX(),(int)event.getY())){
                    paddleX = (int) (event.getX()- paddle.getWidth()/3);
                    paddleY = (int) (event.getY()- paddle.getHeight()/3);
            }
                for(int i=0; i<TOTAL_BRICK; i++){
                    if(!locked[i] && !r2[0].contains((int)event.getX(),(int)event.getY())){
                        x[i] = (int) (event.getX()- b_brick[i].getWidth()/2);
                        y[i] = (int) (event.getY()- b_brick[i].getHeight()/2);
                    }
                    isPressed[0] =false;
                    isPressed[1] = false;

                }
                invalidate();

        }
        return true;
    }

    //set background
    private void readBackground(Context context) {
        background = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.backgroundgen));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        //Play button pressed
        if(plays.contains((int)e.getX(),(int)e.getY())){
            Activity b = (Activity) getContext();
            saveLevel();
            savePaddle();
            mess =  getResources().getString(R.string.saved);
            Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
            b.onBackPressed();

        }

        if(r2[1].contains((int)e.getX(),(int)e.getY())&&k==1){
            paddleLock=false;
            isPressed[1]=true;
        }
        //Selector brick pressed

        if(j==TOTAL_BRICK-1)
            return false;
        if(r2[0].contains((int)e.getX(),(int)e.getY())&&(paddleLock)){
            //First brick
            if(locked[0]&&j==-1){
                locked[0]=false;
                j++;
                isPressed[0] =true;
                return false;
            }
            while (j < TOTAL_BRICK) {
                if (locked[j]) {
                    locked[j + 1] = false;
                    j++;
                    break;
                }
                mess =  getResources().getString(R.string.lock_bricks);
                Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                break;
            }
            isPressed[0] = true;
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        for(int i=0;i<TOTAL_BRICK;i++){
            if(r[i].contains((int)e.getX(),(int)e.getY()) && !locked[i]){
                locked[i]=true;
            }else if(r[i].contains((int)e.getX(),(int)e.getY()) && locked[0]){
                locked[i]=false;
            }
        }
        if(paddleR.contains((int)e.getX(),(int)e.getY()) && !paddleLock){
            k=0;
            paddleLock=true;

        }
        else if(paddleR.contains((int)e.getX(),(int)e.getY()) && paddleLock)
            paddleLock=false;

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void savePaddle(){
        try {
            FileOutputStream fOut = getContext().openFileOutput("paddle.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(String.valueOf(paddleY));
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void saveLevel() {
        String weight;

        weight="";
        try {
            FileOutputStream fOut = getContext().openFileOutput("brick.txt", Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);


            for(int i=0;i<=j;i++){
                weight =weight + (x[i]+", ");
            }
            weight= weight+":";
            for(int i=0;i<=j;i++){
                weight =weight + (y[i]+", ");
            }

            osw.write(weight);
            osw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
