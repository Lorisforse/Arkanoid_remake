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
    private GestureDetector detector;
    //RECT PER I SINGOLI BRICK
    private Rect[] r;
    private Rect[] r2;
    boolean locked[];
    private final static int TOTAL_BRICK =20;
    boolean ispressed;
    private int j;
    private Brick brick;
    private Bitmap play;
    private Rect plays;

    //COSTRUTTORE
    public CustomLevel (Context context)  {
        super(context);
        paint = new Paint();
        readBackground(context);
        detector=new GestureDetector(context, this);


        j=-1;
        x = new int[TOTAL_BRICK];
        y = new int[TOTAL_BRICK];
        b_brick = new Bitmap[TOTAL_BRICK];
        r = new Rect[TOTAL_BRICK];
        r2 = new Rect[3];
        locked = new boolean[TOTAL_BRICK];
        for(int i=0; i<r2.length;i++)
            r2[i] = new Rect();
        for(int i=0; i<TOTAL_BRICK;i++)
            r[i] = new Rect();
        brick = new Brick(context);
        play = BitmapFactory.decodeResource(getResources(), R.drawable.visto);
        play = Bitmap.createScaledBitmap(play, 200,200,false);
        plays= new Rect();


        for(int i=0; i<TOTAL_BRICK; i++){
            brick.skin();
            b_brick[i] = brick.getBrick();
            b_brick[i] = Bitmap.createScaledBitmap(b_brick[i],105,85,false);
            locked[i]=true;
        }
        plays.set(400,1400,650,1700);
        Toast.makeText(getContext(), "Tieni premuto su un brick per bloccarlo", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        // creates a background only once
        if (stretchedOut == null) {
            stretchedOut = Bitmap.createScaledBitmap(background, size.x, size.y, false);
        }
        canvas.drawBitmap(stretchedOut, 0, 0, paint);
        r2[0].set(0,10,125,115);

        if(ispressed){
            paint.setColor(Color.YELLOW);
            canvas.drawRect(r2[0],paint);
        }
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        canvas.drawText("X "+(TOTAL_BRICK-j-1),130,70,paint);


        //DRAW BRICK
        for(int i=0; i<TOTAL_BRICK; i++){
            r[i].set(x[i], y[i],x[i]+200, y[i]+120);
            canvas.drawBitmap(b_brick[i],x[i]+10, y[i]+20, paint);
        }

        canvas.drawBitmap(play, size.x/2-110,1400,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        switch( event.getAction() ){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                for(int i=0; i<TOTAL_BRICK; i++){
                    if(!locked[i] && !r2[0].contains((int)event.getX(),(int)event.getY())){
                        x[i] = (int) (event.getX()- b_brick[i].getWidth()/2);
                        y[i] = (int) (event.getY()- b_brick[i].getHeight()/2);
                    }
                    ispressed=false;
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
            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            b.onBackPressed();

        }

        //Selector brick pressed

        if(j==TOTAL_BRICK-1)
            return false;
        if(r2[0].contains((int)e.getX(),(int)e.getY())){
            //First brick
            if(locked[0]&&j==-1){
                locked[0]=false;
                j++;
                ispressed=true;
                return false;
            }
            while (j < TOTAL_BRICK) {
                if (locked[j]) {
                    locked[j + 1] = false;
                    j++;
                    break;
                }
                Toast.makeText(getContext(), "Blocca tutti i brick prima", Toast.LENGTH_SHORT).show();
                break;
            }
            ispressed = true;
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

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
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
