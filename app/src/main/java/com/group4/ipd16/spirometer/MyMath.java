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
    private static final double FEV1_MULTIPLIER = 1;
    private static final double FVC_MULTIPLIER = 1;
    private static double FILTER = 0.30;
    private static double PEAK_FILTER = 12.00;

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

    public static List<Double> FilterPeakResults(List<Double> listResults){
        for ( int i = listResults.size()-1; i >= 0; i --){
            if(listResults.get(i) > PEAK_FILTER){
                listResults.remove(i);
            }
        }
        return listResults;
    }

    public static List<Double> FilterExpiration(List<Double> listResults) {
        int indexStart = -1;
        int indexEnd = listResults.size() - 1;

        for(int i = 1; i < listResults.size(); i++){
            if(i >= 2){
            if(indexStart == -1 && listResults.get(i) > 0.50 && listResults.get(i) >= listResults.get(i-2) + 0.50 )

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
        catch (IndexOutOfBoundsException e){
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
        fvc *= FVC_MULTIPLIER;
        return fvc;
    }
    public static Double FEV1(List<Double>listResults) { // time between results: 10 ms
        double fev1 = 0;
        int delay = 10; //ms
        int oneSecond = 1000;

        for ( int i = 0; i < oneSecond/delay; i++){
            if(i < listResults.size())
                fev1 += listResults.get(i)*delay;
            else{
               // oneSecond -= i * delay; // one second is different, because measurement is too short
                i = 1000;

            }

        }
        fev1 /= oneSecond;
        fev1 *= FEV1_MULTIPLIER;
        return fev1;
    }
}
