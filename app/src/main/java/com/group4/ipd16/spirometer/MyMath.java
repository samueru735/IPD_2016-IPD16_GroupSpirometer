package com.group4.ipd16.spirometer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Samuel on 11/04/2016.
 */
public class MyMath {
    private static double FILTER = 0.03;

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
        return listResults;
    }

    public static List<Double> FilterExpiration(List<Double> listResults) {
        int indexStart = -1;
        int indexEnd = listResults.size() - 1;

        for(int i = 1; i < listResults.size(); i++){
            if(listResults.get(i) > 0.50 && listResults.get(i) >= listResults.get(i-1) + 0.50 ){
                indexStart = i - 2;
            }
            if(indexStart >= 0 && listResults.get(i) <= 0.3 ){
                if( i < indexEnd){
                    if(listResults.get(i+1) < 0.2){
                        indexEnd = i + 1;
                        i = listResults.size();
                    }
                }
            }
        }
        listResults.subList(indexEnd, listResults.size()).clear();
        listResults.subList(0, indexStart).clear();

        return listResults;
    }
}
