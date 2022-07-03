package com.example.android.arkanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    int difficulty;
    int lifes;
    private CustomLevel customLevel;
    SharedPreferences.Editor edt;

    //FIREBASE
    DatabaseReference messageRef;
    FirebaseDatabase database;

    String roomName ="";
    String playerName= "";
    String role="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        try{

            roomName = getIntent().getStringExtra("roomName");
            playerName = getIntent().getStringExtra("playerName");
            if(roomName.equals(playerName)){
                role = "host";
            }else {
                role = "guest";
            }
            SharedPreferences preferences = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("roomName", roomName);
            editor.putString("role", role);

            editor.apply();
            addRoomEventListener();

        }catch(Exception e){
            Log.e("difficulty","avviata una difficoltà normale");

        }


        checkGameMode();

        difficulty=getIntent().getIntExtra("difficulty",1);
        lifes= getIntent().getIntExtra("lifes",3);
        // set the screen orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // create a new game
        if(difficulty==0){
            customLevel = new CustomLevel(this);
            setContentView(customLevel);
        }else{
            game = new Game(this, lifes, 0, Constants.getGameMode(), difficulty);
            setContentView(game);

            //create a handler and thread
            createHandler();
            myThread = new UpdateThread(updateHandler);
            myThread.start();
        }

    }


    private void checkGameMode() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if (Constants.getGameMode() == 4) {
            if (sharedPref.getInt("gameMode", 4) == 4)
                Constants.setGameMode(0);
            else
                Constants.setGameMode(sharedPref.getInt("gameMode", 4));
        }else{
            edt = sharedPref.edit();
            edt.putInt("gameMode", Constants.getGameMode());
            edt.apply();
        }
    }
    private void createHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onStop(){
        super.onStop();

        if(role.equals("host")){
            messageRef = database.getReference("rooms/" + roomName );
            messageRef.removeValue();
        }else if(role.equals("guest")){
            messageRef = database.getReference("rooms/" + roomName + "/guest");
            messageRef.removeValue();
        }
        super.onBackPressed();
    }
    protected void onPause() {
        super.onPause();
        switchSound();

        if(difficulty!=0)
            game.stopSensing();

    }

    protected void onResume() {
        super.onResume();
        switchSound();

        if(difficulty!=0)
            game.runScanning();
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
//FIREBASE
private void addRoomEventListener(){
        if(role.equals("host"))
            messageRef = database.getReference("rooms/" + roomName + "/guest" + "/name");
        else
            messageRef = database.getReference("rooms/" + roomName + "/host" + "/name");

    messageRef.addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //message received
            if(dataSnapshot.getValue(String.class) == null)
                Toast.makeText(MainActivity.this, "Avversario disconnesso", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}



}
