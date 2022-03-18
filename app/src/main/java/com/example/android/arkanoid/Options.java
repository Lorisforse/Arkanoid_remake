package com.example.android.arkanoid;

import android.content.Context;
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
        setPreferences();

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.italiano:
                if (checked)
                    setLocate("it");
                break;
            case R.id.francese:
                if (checked)
                    setLocate("fr");
                break;
            case R.id.spagnolo:
                if (checked)
                    setLocate("es");
                break;
            case R.id.inglese:
                if (checked)
                    setLocate("en");
                break;
        }
        seekValue = gamemode.getProgress();
        edt.putInt("gameMode", seekValue);
        edt.apply();
        setContentView(R.layout.activity_options);
        setPreferences();

    }

    //funzione che imposta nelle preferenze dell'app, la lingua selezionata
    private void setLocate(String language) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, metrics);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        seekValue = gamemode.getProgress();
        edt.putInt("gameMode", seekValue);
        edt.apply();
        Constants.setGameMode(seekValue);
    }

    //funzione che recupera le preferenze espresse precedentemente
    private void setPreferences() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        edt = sharedPref.edit();
        gamemode = findViewById(R.id.gameMode);
        seekValue = sharedPref.getInt("gameMode", 4);
        if (seekValue == 4) {
            seekValue = 0;
        }
        gamemode.setProgress(seekValue);
    }

    @Override
    public void onResume() {
        switchSound();
        super.onResume();
    }

    public void onPause() {
        switchSound();
        super.onPause();
    }

    public void switchSound() {
        if (Constants.getFlag()) {

            Constants.sound.s_menu.pause();
            Constants.setFlag(false);
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
        } else {
            Constants.sound = new SoundPlayer(this);
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
            Constants.setFlag(true);
        }

    }
}