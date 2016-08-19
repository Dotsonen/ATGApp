package com.example.dotson.atgtipps;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Dotson on 2016-07-26.
 */
public class FileReader {

    int race;
    Context context;
    String dataFromFile;
    String[] sortedArray;

    public FileReader(int race, Context context){
        this.race = race;
        this.context = context;
    }


    public String[] getHorseArray(){

        try {
            FileInputStream fileInputStream = context.openFileInput("race" + race + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

            dataFromFile = reader.readLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sortedArray = splitDataFromFile(dataFromFile);

        return sortedArray;
    }

    public String[] splitDataFromFile(String dataFromFile){

        String[] arr = dataFromFile.split("/");
        return arr;
    }
}
