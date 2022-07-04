package com.example.android.arkanoid.javaClass_activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.arkanoid.R;

public class SplashScreen extends AppCompatActivity {
    int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainMenu.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}