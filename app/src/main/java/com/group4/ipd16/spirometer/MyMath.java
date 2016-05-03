package com.group4.ipd16.spirometer;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 11/04/2016.
 */
public class MyMath {
    private static double FILTER = 0.30;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Double> FilterZeroResults(List<Double> listResults) {

        for(int i = listResults.size()-1; i >= 0; i--){
            if(listResults.get(i) < FILTER && listResults.get(i) > -FILTER){
                listResults.remove(i);
            }
        }
        Log.i("TAG", "Filtered list: " + listResults.toString());
        return listResults;
    }

    public static List<Double> FilterExpiration(List<Double> listResults) {
        int indexStart = -1;
        int indexEnd = listResults.size() - 1;

        for(int i = 1; i < listResults.size(); i++){
            if(indexStart == -1 && listResults.get(i) > 0.50 && listResults.get(i) >= listResults.get(i-2) + 0.50 ){
                if(i >= 2)
                    indexStart = i - 2;
            }
            if(indexStart >= 0 && listResults.get(i) <= 0.3 ){
                if( i < indexEnd){
                    if(listResults.get(i+1) < 0.2){
                        indexEnd = i;
                        i = listResults.size();
                    }
                }
            }
        }
        try{
            listResults.subList(indexEnd, listResults.size()-1).clear();
        }
        catch (ArrayIndexOutOfBoundsException e){
            try{
                listResults.subList(indexEnd--, listResults.size()-1).clear();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }

        try{
            listResults.subList(0, indexStart).clear();
        }
        catch (Exception e){
            Log.i("TAG", "Bad results, try again",e);
        }

        return listResults;
    }

    public static Double FVC(List<Double>listResults) { // time between results: 10 ms
        double fvc = 0;
        int delay = 10; //ms

        List<Double> intermediaryResults = new ArrayList<Double>();
        for ( int i = 0; i < listResults.size(); i++){
            intermediaryResults.add(listResults.get(i)*delay);
        }

        for (double result: intermediaryResults
             ) {
            fvc += result;
        }
        fvc /= 1000;

        return fvc;
    }
}
