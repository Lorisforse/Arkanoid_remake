package com.example.android.arkanoid;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Record extends AppCompatActivity {

    TextView txt[];
    String recordFromGame;
    String[] arraySplitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        txt = new TextView[10];
        /*Declaration of the Textview
        top= txt[9] = best highscore
        bottom= txt[0] = worst highscore
        */
        txt[9] = (TextView) findViewById(R.id.record1);
        txt[8] = (TextView) findViewById(R.id.record2);
        txt[7] = (TextView) findViewById(R.id.record3);
        txt[6] = (TextView) findViewById(R.id.record4);
        txt[5] = (TextView) findViewById(R.id.record5);
        txt[4] = (TextView) findViewById(R.id.record6);
        txt[3] = (TextView) findViewById(R.id.record7);
        txt[2] = (TextView) findViewById(R.id.record8);
        txt[1] = (TextView) findViewById(R.id.record9);
        txt[0] = (TextView) findViewById(R.id.record10);

        //import the record in recordFromGame
        recordFromGame=readFromFile(this);


        //split recordFromGame and insert the values in arraySplitted
        arraySplitted =(recordFromGame.split(", "));

        //create an array of int to sort, lengt=arraySplitted +10 to hold the old records
        int arraySorted[] = new int[arraySplitted.length+10];
        try{
            for(int i = 0; i< arraySplitted.length; i++){
                arraySorted[i] = Integer.parseInt(String.valueOf(arraySplitted[i])); //Note charAt
            }
        }catch (Exception e){
            Toast.makeText(this, "Nessun record da visualizzare",Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }

        Arrays.sort(arraySorted);
        removeCopy(arraySorted);
        Arrays.sort(arraySorted);

        //insert the values into the TextViews
        int j=arraySorted.length-1;
        for(int i=txt.length-1;i>=0;i--){
            txt[i].setText(String.valueOf(arraySorted[j]));
            j--;
        }

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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
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
    public void onResume(){
        switchSound();
        super.onResume();
    }
    public void onPause(){
        switchSound();
        super.onPause();
    }

    public void switchSound(){
        if(Constants.sound.s_menu.isPlaying()){

            Constants.sound.s_menu.pause();
            Constants.setSoundPosition(Constants.sound.s_menu.getCurrentPosition());
        }
        else {
        
            Constants.sound.s_menu.seekTo(Constants.getSoundPosition());
            Constants.sound.playMenu();
        }
    }
}