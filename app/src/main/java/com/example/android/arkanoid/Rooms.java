package com.example.android.arkanoid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rooms extends AppCompatActivity {

    private Button button;
    private ListView listView;
    public TextView loading;
    public ProgressBar progressBar;

    ArrayList<String> roomsList;

    String playerName ="";
    String roomName ="";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    DatabaseReference guestRef;

    boolean opponentConnect =false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);database = FirebaseDatabase.getInstance();

         intent = new Intent(getApplicationContext(), MainActivity.class);
        //get the player name and assign his room name to the player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        roomName = playerName;

        listView = findViewById(R.id.listView);

        loading = findViewById(R.id.loading);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        //all existing available rooms
        roomsList = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opponentListener();


                if(!opponentConnect){
                    //roomRef = database.getReference("rooms/" + roomName + "/host" + "/name");
                    roomRef = database.getReference("rooms/" + roomName);
                    roomName = playerName;
                    addRoomEventListener();
                    roomRef.setValue("");
                }else{
                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("roomName", roomName);
                    intent.putExtra("playerName", playerName);
                    intent.putExtra("difficulty", 5);
                    progressBar.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    button.setText("CREATE ROOM");
                    button.setEnabled(true);
                    listView.setEnabled(true);


                    startActivity(intent);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    //join an existing room and add yourself as player2
                    roomName = roomsList.get(position);
                    opponentListener();

                    if (playerName.equals(roomName))
                        roomRef = database.getReference("rooms/" + roomName + "/host" + "/name");
                    else
                        roomRef = database.getReference("rooms/" + roomName + "/guest" + "/name");
                    addRoomEventListener();
                    roomRef.setValue(playerName);
                    progressBar.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    listView.setEnabled(false);



            }
        });

        //show if new room is available
        addRoomsEventListener();
    }

    private void opponentListener() {
        if (playerName.equals(roomName))
            guestRef = (database.getReference("rooms/" + roomName + "/guest" + "/name"));
        else
            guestRef = (database.getReference("rooms/" + roomName + "/host" + "/name"));
        guestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                opponentConnect = snapshot.getValue() != null;
                if(loading.getVisibility()==View.VISIBLE){
                    if(opponentConnect){

                        button.setText("Play");
                    }
                    else
                        button.setText("CREATE ROOM");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //join the room
                button.setEnabled(true);
                //Todo: togliere button enabled se non serve
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Rooms.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addRoomsEventListener(){
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for(DataSnapshot snapshot : rooms){
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Rooms.this, R.layout.row, roomsList);
                    listView.setAdapter(adapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - nothing
            }
        });
    }

    //todo la room del samsung non scompare
    @Override
    protected void onResume() {
        switchSound();
        opponentConnect=false;
        addRoomsEventListener();
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