package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LevelsMenu extends AppCompatActivity {
    int gameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_screen);
        gameMode = getIntent().getIntExtra("gameMode",0);

    }

    public void easy(View view) {
        openMainActivity(view, 1);
    }

    public void medium(View view) {
        openMainActivity(view, 2);
    }

    public void hard(View view) {
        openMainActivity(view, 3);
    }

    public void custom(View view) {
        openMainActivity(view, 0);
    }

    public void playCustom(View view) {

        openMainActivity(view, 4);

    }


    private void openMainActivity(View view, int difficulty) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}