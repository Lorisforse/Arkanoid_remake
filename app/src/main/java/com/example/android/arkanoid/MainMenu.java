package com.example.android.arkanoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity {
    ImageView music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        music = findViewById(R.id.sound);
        Constants.sound = new SoundPlayer(this);


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.sound.s_menu.isPlaying()) {
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode);
                    Constants.musicActive=false;
                } else {
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                    Constants.musicActive=true;
                }
                switchSound();
            }
        });
    }



    public void onPause(){
        switchSound();
        super.onPause();
    }

    @Override
    public void onResume(){
        switchSound();
        super.onResume();
    }
    public void openDifficultyScreen(View view) {
        Intent intent = new Intent(this, LevelsMenu.class);
        startActivity(intent);
    }

    public void openRecord(View view){
        Intent intent = new Intent(this,Record.class);
        startActivity(intent);
    }

    public void openOptions(View view){
        Intent intent = new Intent(this,Options.class);
        startActivity(intent);
    }

    public void switchSound(){
        if(Constants.sound.s_menu.isPlaying()){

            Constants.sound.s_menu.pause();
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
        }
        else {
            Constants.sound = new SoundPlayer(this);
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
        }
    }
}