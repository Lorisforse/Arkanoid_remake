package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.InputStream;

public class LevelsMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty_screen);

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
        try{
            InputStream inputStream = this.openFileInput("brick.txt");
            inputStream.close();
            openMainActivity(view, 4);
        } catch (Exception e) {
            Toast.makeText(this, "Crea prima un livello con il tasto \"+\"", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteLevel(View view) {
        try {
            InputStream inputStream = this.openFileInput("brick.txt");
            inputStream.close();
            this.deleteFile("brick.txt");
            Toast.makeText(this, "Cancellato", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Non ci sono livelli", Toast.LENGTH_SHORT).show();

        }

    }

    private void openMainActivity(View view, int difficulty) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        switchSound();
        super.onResume();
    }
    public void onPause(){
        switchSound();
        super.onPause();
    }

    public void switchSound(){
        if(Constants.getFlag()){

            Constants.sound.s_menu.pause();
            Constants.setFlag(false);
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
        }
        else {
            Constants.sound = new SoundPlayer(this);
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
            Constants.setFlag(true);
        }
    }
}