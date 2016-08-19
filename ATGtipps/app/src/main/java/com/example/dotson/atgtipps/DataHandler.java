package com.example.dotson.atgtipps;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

/**
 * Created by Dotson on 2016-07-08.
 */
public class DataHandler {

    JSONObject jsonObject, race;
    JSONArray races, starts;
    TextView tv;
    Context context;
    int raceNumber;

    int startNumber, moneyPerStart, averageOdds, money, numberOfStarts, procentage;
    String nameOf;

    private HashMap<Integer, String> startNumberNameOf = new HashMap<>();
    private HashMap<String, Integer> getStartNumberNameOf = new HashMap<>();
    private HashMap<Integer, String> averageNameOf = new HashMap<>();
    private HashMap<Integer, String> moneyNameOf = new HashMap<>();
    private HashMap<Integer, String> procentageNameOf = new HashMap<>();
    private HashMap<String, Integer> getProcentageNameOf = new HashMap<>();
    private HashMap<String, Integer> rankMap = new HashMap<>();
    private HashMap<Integer, String> finalMap = new HashMap<>();

    private int[] averageOddsArr, sortedOdds, moneyArr, sortedMoneyArr,
                  procentageArr, sortedProcentage, startNumberArr,
                  finalRankingArr, sortedFinalRanking;

    public DataHandler(JSONObject jsonObject, Context context, int raceNumber){
        this.jsonObject = jsonObject;
        this.context = context;
        this.raceNumber = raceNumber;
    }

    public void horseNumber(){

        try {
            races = jsonObject.getJSONArray("races");
            race = races.getJSONObject(raceNumber - 1);
            starts = race.getJSONArray("starts");
            averageOddsArr = new int[starts.length()];
            moneyArr = new int[starts.length()];
            procentageArr = new int[starts.length()];
            startNumberArr = new int[starts.length()];

            for(int i = 0; i < starts.length(); i++){
                startNumber = starts.getJSONObject(i).getInt("number");
                nameOf = starts.getJSONObject(i).getJSONObject("horse").getString("name");
                money = starts.getJSONObject(i).getJSONObject("horse").getInt("money");
                numberOfStarts = starts.getJSONObject(i).getJSONObject("horse").getJSONObject("statistics").getJSONObject("life").getInt("starts");
                moneyPerStart = money / numberOfStarts;
                averageOdds = starts.getJSONObject(i).getJSONObject("horse").getJSONObject("statistics").getJSONObject("lastFiveStarts").getInt("averageOdds");
                procentage = starts.getJSONObject(i).getJSONObject("pools").getJSONObject("V75").getInt("betDistribution");

                startNumberNameOf.put(startNumber,nameOf);
                getStartNumberNameOf.put(nameOf,startNumber);

                while(averageNameOf.containsKey(averageOdds)){
                    averageOdds += 1;
                }
                averageNameOf.put(averageOdds, nameOf);

                while(moneyNameOf.containsKey(moneyPerStart)){
                    moneyPerStart += 1;
                }
                moneyNameOf.put(moneyPerStart, nameOf);

                while(procentageNameOf.containsKey(procentage)){
                    procentage += 1;
                }
                procentageNameOf.put(procentage, nameOf);
                getProcentageNameOf.put(nameOf, procentage);


                averageOddsArr[i] = averageOdds;
                moneyArr[i] = moneyPerStart;
                procentageArr[i] = procentage;
                startNumberArr[i] = startNumber;
            }



            sortedOdds = insertionSort(averageOddsArr);
            sortedMoneyArr = reverseInsertionSort(moneyArr);
            sortedProcentage = reverseInsertionSort(procentageArr);

            rankHorses();

            writeToFile();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public int[] insertionSort(int[] input){
        int temp;
        for (int i = 1; i < input.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(input[j] < input[j-1]){
                    temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;
                }
            }
        }
        return input;
    }

    public int[] reverseInsertionSort(int[] input){
        int temp;
        for (int i = 1; i < input.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(input[j] > input[j-1]){
                    temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;
                }
            }
        }
        return input;
    }

    public void rankHorses(){

        int tempRank = 1;
        String tempName = "", results = "", test = "";
        finalRankingArr = new int[startNumberArr.length];

        for(int i = 0; i < sortedProcentage.length; i++){
            tempName = procentageNameOf.get(sortedProcentage[i]);
            rankMap.put(tempName,i);

        }

        for(int j = 0; j < sortedMoneyArr.length; j++){
            tempName = moneyNameOf.get(sortedMoneyArr[j]);
            tempRank = rankMap.get(moneyNameOf.get(sortedMoneyArr[j])) + j;
            rankMap.put(tempName, tempRank);
        }

        for(int k = 0; k < sortedOdds.length; k++){
            tempName = averageNameOf.get(sortedOdds[k]);
            tempRank = rankMap.get(tempName) + k;

            while(finalMap.containsKey(tempRank)) tempRank += 1;

            rankMap.put(tempName, tempRank);

            finalMap.put(tempRank, tempName);
        }

        for(int i = 0; i < startNumberArr.length; i++){

            tempName = startNumberNameOf.get(startNumberArr[i]);
            tempRank = rankMap.get(tempName);

            finalRankingArr[i] = tempRank;

        }

        sortedFinalRanking = insertionSort(finalRankingArr);

        for(int i = 0; i < sortedFinalRanking.length; i++){

            results += finalMap.get(sortedFinalRanking[i]);
        }
    }

    public void writeToFile(){

        String nameOfHorse;
        int startNumber;
        double betDistribution, hundred = 100;
        FileOutputStream fileOutputStream;
        String filename = "race"+ raceNumber  +".txt";

        try {
            fileOutputStream =  context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        for(int i = 0; i < sortedFinalRanking.length; i++) {

            nameOfHorse = finalMap.get(sortedFinalRanking[i]);
            startNumber = getStartNumberNameOf.get(nameOfHorse);
            betDistribution = getProcentageNameOf.get(nameOfHorse) / hundred;

            bufferedWriter.write(startNumber + " " + nameOfHorse + "     " + betDistribution + "%" + "/");
            bufferedWriter.flush();

        }

            bufferedWriter.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
