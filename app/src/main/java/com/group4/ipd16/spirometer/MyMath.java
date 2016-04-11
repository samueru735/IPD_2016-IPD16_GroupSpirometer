package com.group4.ipd16.spirometer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Samuel on 11/04/2016.
 */
public class MyMath {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static List<Double> FilterResults(List<Double> listResults) {

        for(int i = listResults.size()-1; i >= 0; i--){
            if(listResults.get(i) < 2 && listResults.get(i) > -2){
                listResults.remove(i);
            }
        }
        return listResults;
    }
}
