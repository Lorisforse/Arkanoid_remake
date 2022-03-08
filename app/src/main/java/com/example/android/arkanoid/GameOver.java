package com.example.android.arkanoid;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameOver extends AppCompatActivity {
    private int gameMode;
    private int difficulty;
    private Game game;
    private CustomLevel customLevel;
    private Handler updateHandler;
    private UpdateThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        gameMode = getIntent().getIntExtra("gameMode",0);
        difficulty = getIntent().getIntExtra("difficulty",1);

    }

    public void tryAgain(View view) {
        if(difficulty==0){
            customLevel = new CustomLevel(this);
            setContentView(customLevel);
        }else{
            game = new Game(this, 3, 0, gameMode, difficulty);
            setContentView(game);

            //create a handler and thread
            createHandler();
            myThread = new UpdateThread(updateHandler);
            myThread.start();
        }

    }

    public void back(View view) {
        Intent intent = new Intent(this,MainMenu.class);
        intent.putExtra("gameMode", gameMode);
        startActivity(intent);
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


}