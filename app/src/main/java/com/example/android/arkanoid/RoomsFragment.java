package com.example.android.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomsFragment extends Fragment implements View.OnClickListener{

    private Button roomsPlay, back;
    private ListView listView;
    public TextView loading;
    public ProgressBar progressBar;

    ArrayList<String> roomsList;

    String roomName ="";



    boolean opponentConnect =false;
    Intent intent;
    ImageView viewImage;



    String playerName="";
    private static final String TAG = "OfflineActivity";
    FirebaseDatabase database;
    DatabaseReference playerRef;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    DatabaseReference guestRef;

    public RoomsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_rooms, container, false);


        database = FirebaseDatabase.getInstance();
        viewImage = (ImageView) v.findViewById(R.id.background);
        viewImage.getBackground().setAlpha(0);

        back = (Button) v.findViewById(R.id.back);


        listView = (ListView) v.findViewById(R.id.listRooms);
        intent = new Intent(getContext(), MainActivity.class);
        //get the player name and assign his room name to the player name
        SharedPreferences preferences = this.requireActivity().getSharedPreferences("PREFS", 0);

        playerName = preferences.getString("playerName", "");
        roomName = playerName;


        loading = v.findViewById(R.id.loading);
        progressBar = v.findViewById(R.id.progressBar);
        roomsPlay = v.findViewById(R.id.rooms_play);

        //all existing available rooms
        roomsList = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });

        roomsPlay.setOnClickListener(new View.OnClickListener() {
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
                    roomsPlay.setText(getString(R.string.create_room));
                    roomsPlay.setEnabled(true);
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
                listView.setEnabled(false);

                viewImage.getBackground().setAlpha(255);


            }
        });

        //show if new room is available
        addRoomsEventListener();










        return v;
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

                        roomsPlay.setText(getString(R.string.play));
                    }
                    else
                        roomsPlay.setText(getString(R.string.create_room));
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
                //button.setEnabled(true);
                //Todo: togliere button enabled se non serve
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.row, roomsList);
                    listView.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error - nothing
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}