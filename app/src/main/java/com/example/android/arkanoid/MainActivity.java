package com.example.android.arkanoid;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    int gameMode;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        gameMode=getIntent().getIntExtra("gameMode",0);
        difficulty=getIntent().getIntExtra("difficulty",1);

        // set the screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // create a new game
        game = new Game(this, 3, 0, gameMode, difficulty);
        setContentView(game);

        //create a handler and thread
        createHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        game.stopSensing();
    }

    protected void onResume() {
        super.onResume();
        game.runScanning();
    }

}
