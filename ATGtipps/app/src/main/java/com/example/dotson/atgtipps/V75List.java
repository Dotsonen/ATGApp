package com.example.dotson.atgtipps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class V75List extends AppCompatActivity {


    TextView tv;
    ListView lv;
    String dataFromFile;
    int race;
    String[] sortedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v75_list);

        lv = (ListView) findViewById(R.id.listView);
        race = getIntent().getExtras().getInt("race");


        try {
            FileInputStream fileInputStream = openFileInput("race" + race + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            dataFromFile = reader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortedArray = splitDataFromFile(dataFromFile);
       // tv.setText(sortedArray[1]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sortedArray);
        lv.setAdapter(adapter);


    }

    public String[] splitDataFromFile(String dataFromFile){

       String[] arr = dataFromFile.split("/");
        return arr;
    }
}
