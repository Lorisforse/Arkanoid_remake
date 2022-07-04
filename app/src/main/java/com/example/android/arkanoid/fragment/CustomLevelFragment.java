package com.example.android.arkanoid.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.android.arkanoid.javaClass_activity.MainActivity;
import com.example.android.arkanoid.R;

import java.io.InputStream;

/*
    Fragment to manage custom levels, allows you to create a new level, delete it and play it
 */
public class CustomLevelFragment extends Fragment implements View.OnClickListener{

    private Button crea,play,elimina;
    public CustomLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom_level, container, false);

        crea = (Button) v.findViewById(R.id.crea);
        play = (Button) v.findViewById(R.id.play);
        elimina = (Button) v.findViewById(R.id.elimina);

        crea.setOnClickListener(this);
        play.setOnClickListener(this);
        elimina.setOnClickListener(this);

        crea.getBackground().mutate().mutate().setAlpha(64);
        play.getBackground().mutate().setAlpha(255);
        elimina.getBackground().mutate().mutate().setAlpha(64);


        return v;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crea:
                crea.getBackground().mutate().setAlpha(255);
                play.getBackground().mutate().setAlpha(64);
                elimina.getBackground().mutate().setAlpha(64);
                custom(v);
                break;
            case R.id.play:
                crea.getBackground().mutate().setAlpha(64);
                play.getBackground().mutate().setAlpha(255);
                elimina.getBackground().mutate().setAlpha(64);
                playCustom(v);
                break;
            case R.id.elimina:
                crea.getBackground().mutate().setAlpha(64);
                play.getBackground().mutate().setAlpha(64);
                elimina.getBackground().mutate().setAlpha(255);
                deleteLevel(v);
                break;
        }
    }

    //Create a new level
    public void custom(View view) {
        openMainActivity(view, 0);
    }

    //Play the level created
    public void playCustom(View view) {
        try{
            InputStream inputStream = requireContext().openFileInput("brick.txt");
            inputStream.close();
            openMainActivity( view, 4);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Crea prima un livello con il tasto \"+\"", Toast.LENGTH_SHORT).show();
        }
    }
    //Delete the level
    public void deleteLevel(View v) {
        try {
            InputStream inputStream = requireContext().openFileInput("brick.txt");
            inputStream.close();
            requireContext().deleteFile("brick.txt");
            Toast.makeText(getContext(), getResources().getString(R.string.delete), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Non ci sono livelli", Toast.LENGTH_SHORT).show();

        }

    }

    private void openMainActivity(View view, int difficulty) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("difficulty", difficulty);
        startActivity(intent);
    }

}