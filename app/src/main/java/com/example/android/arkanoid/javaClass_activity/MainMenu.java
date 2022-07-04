package com.example.android.arkanoid.javaClass_activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.arkanoid.R;
import com.example.android.arkanoid.fragment.CustomLevelFragment;
import com.example.android.arkanoid.fragment.HomeFragment;
import com.example.android.arkanoid.fragment.RecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Setting Fragment
        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFragment());

        Constants.sound = new SoundPlayer(this);



    }

    private  BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new HomeFragment());
                        return true;
                    case R.id.customize:
                        loadFragment(new CustomLevelFragment());
                        return true;
                    case R.id.score:
                        loadFragment(new RecordFragment());
                        return true;
                    case R.id.options:
                        loadFragment(new SettingsFragment());
                        return true;
                }
                return false;
            };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    public void onPause(){
        switchSound();
        super.onPause();
    }

    @Override
    public void onResume(){
        switchSound();
        super.onResume();
    }
}

