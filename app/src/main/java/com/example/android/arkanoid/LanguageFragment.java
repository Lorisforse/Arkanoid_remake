package com.example.android.arkanoid;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Locale;


public class LanguageFragment extends Fragment implements View.OnClickListener{

    private Button italian,english,spanish,francais, back,check, close;

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_language, container, false);
        italian =(Button) v.findViewById(R.id.italian);
        english =(Button) v.findViewById(R.id.english);
        spanish =(Button) v.findViewById(R.id.spanish);
        francais =(Button) v.findViewById(R.id.francais);
        back =(Button) v.findViewById(R.id.back);
        check =(Button) v.findViewById(R.id.check);
        close =(Button) v.findViewById(R.id.close);

        italian.setOnClickListener(this);
        english.setOnClickListener(this);
        spanish.setOnClickListener(this);
        francais.setOnClickListener(this);
        back.setOnClickListener(this);
        check.setOnClickListener(this);
        close.setOnClickListener(this);
        italian.getBackground().mutate().setAlpha(64);
        english.getBackground().mutate().setAlpha(64);
        spanish.getBackground().mutate().setAlpha(64);
        francais.getBackground().mutate().setAlpha(64);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.italian:
                setLocate("it");
                Locale.getDefault().getDisplayLanguage();
                italian.getBackground().mutate().setAlpha(255);
                english.getBackground().mutate().setAlpha(64);
                spanish.getBackground().mutate().setAlpha(64);
                francais.getBackground().mutate().setAlpha(64);

                break;
            case R.id.english:
                setLocate("en");
                italian.getBackground().mutate().setAlpha(64);
                english.getBackground().mutate().setAlpha(255);
                spanish.getBackground().mutate().setAlpha(64);
                francais.getBackground().mutate().setAlpha(64);
                break;
            case R.id.spanish:
                setLocate("es");
                italian.getBackground().mutate().setAlpha(64);
                english.getBackground().mutate().setAlpha(64);
                spanish.getBackground().mutate().setAlpha(255);
                francais.getBackground().mutate().setAlpha(64);
                break;
            case R.id.francais:
                setLocate("fr");
                italian.getBackground().mutate().setAlpha(64);
                english.getBackground().mutate().setAlpha(64);
                spanish.getBackground().mutate().setAlpha(64);
                francais.getBackground().mutate().setAlpha(255);
                break;
            case R.id.back:
            case R.id.close:
                setLocate("locale");
                requireActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
            case R.id.check:
                if ((italian.getBackground().getAlpha() == english.getBackground().getAlpha()) == (spanish.getBackground().getAlpha() == francais.getBackground().getAlpha()))
                    Toast.makeText(getContext(), "Scegli una lingua prima di confermare", Toast.LENGTH_SHORT).show();
                else
                    requireActivity().getSupportFragmentManager().popBackStackImmediate();
                break;
        }

    }


    private void setLocate(String language) {

        Locale locale = new Locale(language);
        Configuration config = new Configuration();
        config.locale = locale;
        requireContext().getResources().updateConfiguration(config,
                getContext().getResources().getDisplayMetrics());

    }
}