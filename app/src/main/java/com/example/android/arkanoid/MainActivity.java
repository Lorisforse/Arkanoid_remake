package com.example.android.arkanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    int difficulty;
    private CustomLevel customLevel;
    SharedPreferences.Editor edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkGameMode();
        difficulty=getIntent().getIntExtra("difficulty",1);

        // set the screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // create a new game
        if(difficulty==0){
            customLevel = new CustomLevel(this);
            setContentView(customLevel);
        }else{
            game = new Game(this, 3, 0, Constants.getGameMode(), difficulty);
            setContentView(game);

            //create a handler and thread
            createHandler();
            myThread = new UpdateThread(updateHandler);
            myThread.start();
        }

    }


    private void checkGameMode() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if (Constants.getGameMode() == 4) {
            if (sharedPref.getInt("gameMode", 4) == 4)
                Constants.setGameMode(0);
            else
                Constants.setGameMode(sharedPref.getInt("gameMode", 4));
        }else{
            edt = sharedPref.edit();
            edt.putInt("gameMode", Constants.getGameMode());
            edt.apply();
        }
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
        switchSound();

        if(difficulty!=0)
            game.stopSensing();

    }

    protected void onResume() {
        super.onResume();
        switchSound();

        if(difficulty!=0)
            game.runScanning();
    }

    public void switchSound(){
        if(Constants.sound.s_menu.isPlaying()){
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());

            Constants.sound.s_menu.pause();
        }
        else {
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
        }
    }

}
