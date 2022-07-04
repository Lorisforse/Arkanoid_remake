package com.example.android.arkanoid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.android.arkanoid.R;

/*
    Fragment to explain the game modes
 */
public class InfoFragment extends Fragment implements View.OnClickListener{

    private Button back;
    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        back = (Button) v.findViewById(R.id.back);
        back.setOnClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.back)
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
        }
}