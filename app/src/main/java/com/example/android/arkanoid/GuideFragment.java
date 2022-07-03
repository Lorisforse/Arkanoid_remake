package com.example.android.arkanoid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class GuideFragment extends Fragment implements View.OnClickListener{

    Button back;
    public GuideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guide, container, false);
        back = v.findViewById(R.id.back);
        back.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back)
            requireActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}
