package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.content.res.Configuration;
import java.util.*;

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

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.italiano:
                if(checked)
                    setLocate("it");
                setContentView(R.layout.activity_options);
                break;
            case R.id.francese:
                if(checked)
                    setLocate("fr");
                setContentView(R.layout.activity_options);
                break;
            case R.id.spagnolo:
                if(checked)
                    setLocate("es");
                setContentView(R.layout.activity_options);
                break;
            case R.id.inglese:
                if(checked)
                    setLocate("en");
                setContentView(R.layout.activity_options);
                break;
        }
    }

    //funzione che imposta nelle preferenze dell'app, la lingua selezionata
    private void setLocate(String language) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,metrics);
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