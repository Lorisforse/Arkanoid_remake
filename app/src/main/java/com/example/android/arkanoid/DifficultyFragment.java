package com.example.android.arkanoid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DifficultyFragment extends Fragment implements View.OnClickListener {

    Button easy, medium, hard, back;
    Intent intent ;

    public DifficultyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_difficulty, container, false);

        back = (Button) v.findViewById(R.id.back);
        back.setOnClickListener(this);

        easy = (Button) v.findViewById(R.id.easy);
        easy.setOnClickListener(this);

        medium = (Button) v.findViewById(R.id.medium);
        medium.setOnClickListener(this);

        hard = (Button) v.findViewById(R.id.hard);
        hard.setOnClickListener(this);

        intent = new Intent(getContext(), MainActivity.class);
        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easy:
                intent.putExtra("difficulty", 1);
                startActivity(intent);
                break;
            case R.id.medium:
                intent.putExtra("difficulty", 2);
                startActivity(intent);
                break;
            case R.id.hard:
                intent.putExtra("difficulty", 3);
                startActivity(intent);
                break;
            case R.id.back:
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
        }
    }
}