package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class Options extends AppCompatActivity {

    SeekBar gamemode;
    int seekValue;
    SharedPreferences.Editor edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        seekValue=0;

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        edt = sharedPref.edit();

        gamemode = findViewById(R.id.gameMode);
        try{
            seekValue= sharedPref.getInt("gameMode", 0);
        }catch(Exception e){
            e.printStackTrace();
        }
        gamemode.setProgress(seekValue);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainMenu.class);
        seekValue = gamemode.getProgress();
        edt.putInt("gameMode", seekValue);
        edt.apply();;
        intent.putExtra("gameMode", seekValue);
        startActivity(intent);
    }
}