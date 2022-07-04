package com.example.android.arkanoid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class GameModeFragment extends Fragment implements View.OnClickListener {

    private Button touch, controller, dinamico, back, guide;
    SharedPreferences.Editor edt;

    public GameModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_mode, container, false);
        touch = (Button) v.findViewById(R.id.touch);
        controller = (Button) v.findViewById(R.id.controller);
        dinamico = (Button) v.findViewById(R.id.dinamico);
        back = (Button) v.findViewById(R.id.back);
        guide = (Button) v.findViewById(R.id.guide);

        touch.setOnClickListener(this);
        controller.setOnClickListener(this);
        dinamico.setOnClickListener(this);
        back.setOnClickListener(this);
        guide.setOnClickListener(this);

        touch.getBackground().mutate().setAlpha(64);
        controller.getBackground().mutate().setAlpha(64);
        dinamico.getBackground().mutate().setAlpha(64);

        setPreferences();
        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.touch:
                touch.getBackground().mutate().setAlpha(255);
                controller.getBackground().mutate().setAlpha(64);
                dinamico.getBackground().mutate().setAlpha(64);
                edt.putInt("gameMode", 2);
                Constants.setGameMode(2);
                break;
            case R.id.controller:
                touch.getBackground().mutate().setAlpha(64);
                controller.getBackground().mutate().setAlpha(255);
                dinamico.getBackground().mutate().setAlpha(64);
                edt.putInt("gameMode", 1);
                Constants.setGameMode(1);
                break;
            case R.id.dinamico:
                touch.getBackground().mutate().setAlpha(64);
                controller.getBackground().mutate().setAlpha(64);
                dinamico.getBackground().mutate().setAlpha(255);
                edt.putInt("gameMode", 0);
                Constants.setGameMode(0);
                break;
            case R.id.back:
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.guide:
                loadFragment(new GuideFragment());
                break;
        }
        edt.apply();

    }


    private void setPreferences() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        edt = sharedPref.edit();
        switch (sharedPref.getInt("gameMode", 4)) {
            case 0:
                dinamico.getBackground().mutate().setAlpha(255);
                break;
            case 1:
                controller.getBackground().mutate().setAlpha(255);
                break;
            case 2:
                touch.getBackground().mutate().setAlpha(255);
                break;
        }

    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, "info")
                .addToBackStack(null)
                .commit();
    }
}