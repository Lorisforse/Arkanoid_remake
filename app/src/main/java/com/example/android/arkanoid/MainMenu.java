package com.example.android.arkanoid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainMenu extends AppCompatActivity {
    private SoundPlayer sound;
    ImageView music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        music = findViewById(R.id.sound);

        if (!Constants.getFlag()) {
            sound = new SoundPlayer(this);
            sound.playMenu();
            Constants.setFlag(true);
        }


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.getFlag()){
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode);
                }else{
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                }
                switchSound();

            }
        });


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
        if(Constants.getFlag()){

            sound.s_menu.stop();
            Constants.setFlag(false);
        }
        else if(!Constants.getFlag()){
            sound = new SoundPlayer(this);
            sound.playMenu();
            Constants.setFlag(true);
        }
    }
}