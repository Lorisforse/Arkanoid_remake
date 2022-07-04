package com.example.android.arkanoid.javaClass_activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.arkanoid.R;
import com.example.android.arkanoid.fragment.GameModeFragment;
import com.example.android.arkanoid.fragment.InfoFragment;
import com.example.android.arkanoid.fragment.LanguageFragment;


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button audio, language, gameMode, info;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        audio = (Button) v.findViewById(R.id.audio);
        audio.setOnClickListener(this);
        language = (Button) v.findViewById(R.id.language);
        language.setOnClickListener(this);
        gameMode = (Button) v.findViewById(R.id.gamemode);
        gameMode.setOnClickListener(this);
        info = (Button) v.findViewById(R.id.info);
        info.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audio:
                if(Constants.sound.s_menu.isPlaying()){
                    audio.getBackground().mutate().setAlpha(64);
                    Constants.sound.s_menu.pause();
                    Constants.musicActive=false;
                    Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
                    audio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.volume_off_24px, 0);
                }
                else {
                    audio.getBackground().mutate().setAlpha(255);
                    audio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.volume_up_24px, 0);

                    Constants.musicActive=true;
                    Constants.sound.playMenu();
                }
                break;
            case R.id.language:
                loadFragment(new LanguageFragment());
                break;

            case R.id.gamemode:
                loadFragment(new GameModeFragment());
                break;
            case R.id.info:
                loadFragment(new InfoFragment());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, "language")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        if(!Constants.sound.s_menu.isPlaying()){
            audio.getBackground().mutate().setAlpha(64);
        }
        super.onResume();
    }
}