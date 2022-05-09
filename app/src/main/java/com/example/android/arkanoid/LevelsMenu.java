package com.example.android.arkanoid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class LevelsMenu extends AppCompatActivity {
    String playerName ="";
    TextView lifes;
    SeekBar lifeChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_menu);
        lifes = findViewById(R.id.diff);
        lifeChoose = findViewById(R.id.difficulty);
        //get the player name and assign his room name to the player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        Toast.makeText(LevelsMenu.this, "Already logged as: " + playerName,Toast.LENGTH_SHORT).show();


        lifeChoose.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lifes.setVisibility(View.INVISIBLE);
                lifeChoose.setVisibility(View.INVISIBLE);
                openMainActivity( 4, lifeChoose.getProgress());
            }
        });
    }

    public void online(View view){
        Intent intent = new Intent(this,Rooms.class);
        startActivity(intent);
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
            lifes.setVisibility(View.VISIBLE);
            lifeChoose.setVisibility(View.VISIBLE);
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
    private void openMainActivity( int difficulty, int lifes) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("lifes", lifes+1);
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
        if(Constants.sound.s_menu.isPlaying()){
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());

            Constants.sound.s_menu.pause();
        }
        else {
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
        }
    }
}