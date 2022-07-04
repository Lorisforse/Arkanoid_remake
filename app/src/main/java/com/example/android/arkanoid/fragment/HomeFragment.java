package com.example.android.arkanoid.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android.arkanoid.R;
import com.example.android.arkanoid.fragment.DifficultyFragment;
import com.example.android.arkanoid.fragment.ExitFragment;
import com.example.android.arkanoid.fragment.RoomsFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
    Fragment to play offline, online, go out and share app link
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button play;
    private EditText nickname;
    Animation scaleUp,scaleDown;




    String playerName="";
    FirebaseDatabase database;
    DatabaseReference playerRef;


    public HomeFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        Button logout = (Button) v.findViewById(R.id.logout);
        logout.setOnClickListener(this);

        play = (Button) v.findViewById(R.id.play);
        play.setOnClickListener(this);

        Button share = (Button) v.findViewById(R.id.share);
        share.setOnClickListener(this);

        Button online = (Button) v.findViewById(R.id.online);
        online.setOnClickListener(this);

        nickname = (EditText) v.findViewById(R.id.nickname);
        nickname.setOnClickListener(this);

        scaleUp = AnimationUtils.loadAnimation(getContext(),R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(getContext(),R.anim.scale_down);

        play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    play.startAnimation(scaleUp);
                else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    play.startAnimation(scaleDown);
                loadFragment(new DifficultyFragment());
                return true;
            }

        });
        nickname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    playerName= nickname.getText().toString();
                    addEventListener();
                    Toast.makeText(getContext(), "Logged as: " + playerName, Toast.LENGTH_SHORT).show();
                    loadFragment(new RoomsFragment());
                }
                return handled;
            }
        });

        database = FirebaseDatabase.getInstance();
        //Check if the player exists and get reference
        SharedPreferences preferences = this.requireActivity().getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName","");

        //the first time is not executed
        if (!playerName.equals("")) {
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }


        playerRef = database.getReference("players/" + playerName);
        addEventListener();
        playerRef.setValue("");
        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                loadFragment(new ExitFragment());

                //System.exit(0);
                break;
            case R.id.share:
                shareTextOnly();
                break;

            case R.id.online:
                nickname.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void shareTextOnly() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        intent.putExtra(Intent.EXTRA_TEXT, "Ti sfido ad un duello su Arkanoid --> scaricalo da qui:@linkPlayStore");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, "exit")
                .addToBackStack(null)
                .commit();
    }


    private void addEventListener() {
        //read from database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //success = continue to the next screen after saving the player name
                if (!playerName.equals("")) {
                    SharedPreferences preferences =  getContext().getSharedPreferences("PREFS", 0);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "ERRORE", Toast.LENGTH_SHORT).show();
            }
        });
    }
}