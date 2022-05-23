package com.example.android.arkanoid;

import android.content.Intent;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {
    ImageView music;
    ImageView wifi;
    EditText username;
    String playerName="";
    ImageButton share;
    private static final String TAG = "OfflineActivity";
    FirebaseDatabase database;
    DatabaseReference playerRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        music = findViewById(R.id.sound);
        wifi = findViewById(R.id.wifi);
        username = findViewById(R.id.username);
        share = findViewById(R.id.share);
        Constants.sound = new SoundPlayer(this);
        database = FirebaseDatabase.getInstance();

        //Check if the player exists and get reference
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName","");


        //la prima volta che apri l'app non viene eseguito questo if
        if(!playerName.equals("")){
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTextOnly("Ti sfido ad un duello su Arkanoid --> scaricalo da qui:@linkPlayStore");
            }
        });
        wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playerName.equals("")){
                    if((username.getText()).toString().equals("")){
                        username.setVisibility(View.VISIBLE);
                        wifi.setImageResource(android.R.drawable.ic_menu_save);
                    }
                    else if(!((username.getText()).toString().equals(""))){
                        username.setVisibility(View.INVISIBLE);
                        playerName = username.getText().toString();
                        playerRef= database.getReference("players/" + playerName);
                        addEventListener();
                        playerRef.setValue("");
                        wifi.setImageResource(R.drawable.wifi);
                        Toast.makeText(MainMenu.this, "Logged as: " + playerName,Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(MainMenu.this, "Already logged as: " + playerName,Toast.LENGTH_SHORT).show();

            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constants.sound.s_menu.isPlaying()) {
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode);
                    Constants.musicActive=false;
                } else {
                    music.setImageResource(android.R.drawable.ic_lock_silent_mode_off);
                    Constants.musicActive=true;
                }
                switchSound();
            }
        });
    }




    private void addEventListener() {
        //read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //success = continue to the next screen after saving the player name
                if (!playerName.equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainMenu.this, "ERRORE", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void shareTextOnly(String title) {
        String sharebody = title;

        // The value which we will sending through data via
        // other applications is defined
        // via the Intent.ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);

        // setting type of data shared as text
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // Adding the text to share using putExtra
        intent.putExtra(Intent.EXTRA_TEXT, sharebody);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }





    public void onPause(){
        switchSound();
        super.onPause();
    }

    @Override
    public void onResume(){
        switchSound();
        super.onResume();
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
        if(Constants.sound.s_menu.isPlaying()){

            Constants.sound.s_menu.pause();
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
        }
        else {
            Constants.sound = new SoundPlayer(this);
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
        }
    }


}