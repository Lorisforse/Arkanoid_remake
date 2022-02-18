package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {
    int gameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        gameMode = getIntent().getIntExtra("gameMode",5);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt;
        edt = sharedPref.edit();

        //options not opened
        if(gameMode==5){
            gameMode= sharedPref.getInt("gameMode", 4);
            //options never opened
            if(gameMode==4){
                gameMode=0;
            }
            //options opened
        }else{
            edt.putInt("gameMode", gameMode);
            edt.apply();
        }
    }

    /*public void openMainActivity(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("gameMode", gameMode);
        startActivity(intent);
    }*/

    public void openDifficultyScreen(View view) {
        Intent intent = new Intent(this, LevelsMenu.class);
        intent.putExtra("gameMode", gameMode);
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
}