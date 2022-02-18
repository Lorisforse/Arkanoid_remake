package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LevelsMenu extends AppCompatActivity {
    int gameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_screen);
        gameMode = getIntent().getIntExtra("gameMode",0);

       /* SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edt;
        edt = sharedPref.edit();*/
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

    private void openMainActivity(View view, int difficulty) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }
}