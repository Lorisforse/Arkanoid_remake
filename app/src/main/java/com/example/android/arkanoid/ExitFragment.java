package com.example.android.arkanoid;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


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
        no = (Button) v.findViewById(R.id.no);
        no.setOnClickListener(this);
        yes.getBackground().setAlpha(64);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.no:
                getFragmentManager().popBackStackImmediate();
                break;
            case R.id.yes:
                yes.getBackground().setAlpha(255);
                no.getBackground().setAlpha(64);
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