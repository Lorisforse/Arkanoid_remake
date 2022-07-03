package com.example.android.arkanoid;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class RecordFragment extends Fragment implements View.OnClickListener{
    private ArrayList<String> recordList;
    private ListView listView;
    private String recordFromGame;
    private String[] arraySplitted;

    public RecordFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_record, container, false);

        listView = v.findViewById(R.id.listRecord);
        recordList = new ArrayList<>();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), R.layout.row, recordList);
        listView.setAdapter(adapter);
        // Inflate the layout for this fragment

        //import the record in recordFromGame
        recordFromGame = readFromFile(v.getContext());


        //split recordFromGame and insert the values in arraySplitted
        arraySplitted = (recordFromGame.split(", "));

        //create an array of int to sort, lengt=arraySplitted +10 to hold the old records
        int[] arraySorted = new int[arraySplitted.length + 10];
        try {
            for (int i = 0; i < arraySplitted.length; i++) {
                arraySorted[i] = Integer.parseInt(String.valueOf(arraySplitted[i])); //Note charAt
            }
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Nessun record da visualizzare", Toast.LENGTH_LONG).show();
            //requireActivity().getFragmentManager().popBackStack();
        }

        Arrays.sort(arraySorted);
        removeCopy(arraySorted);
        Arrays.sort(arraySorted);

        //insert the values into the TextViews
        int j = arraySorted.length - 1;

        for (int i = 10; i >= 0; i--) {
            recordList.add(String.valueOf(arraySorted[j])+" "+getString(R.string.points));
            j--;
        }



        return v;
    }

    //Read the old records from record.txt
    private String readFromFile(Context context) {

        String result = "";

        try {
            InputStream inputStream = context.openFileInput("record.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("").append(receiveString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    //Remove duplicates from a sorted array of int
    public void removeCopy(int array[]){
        for(int i=0;i<array.length-1;i++){
            if(array[i]==array[i+1])
                array[i]=0;
        }

    }

    @Override
    public void onClick(View view) {

    }
}