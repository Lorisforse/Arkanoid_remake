package com.example.android.arkanoid.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.android.arkanoid.R;

import java.util.Objects;

/*
    Fragment to exit the app
 */
public class ExitFragment extends Fragment implements View.OnClickListener {

    private Button yes, no;
    public ExitFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exit, container, false);


        yes = (Button) v.findViewById(R.id.yes);
        yes.setOnClickListener(this);
        yes.getBackground().mutate().setAlpha(64);


        no = (Button) v.findViewById(R.id.no);
        no.setOnClickListener(this);

        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no:
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.yes:
                yes.getBackground().mutate().setAlpha(255);
                no.getBackground().mutate().setAlpha(64);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                }, 500);

                break;



        }
    }
}