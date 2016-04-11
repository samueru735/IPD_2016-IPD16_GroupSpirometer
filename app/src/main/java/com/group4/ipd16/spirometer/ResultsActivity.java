package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    BluetoothConnection btConn;
    Intent intent;
    private TextView tvResult, tvSentData, tvConnStatus;
    private double[] resultsArray;
    private List<Double> listResults = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Bundle b = this.getIntent().getExtras();
        try{
            resultsArray =  b.getDoubleArray("results");
            Double[] doubleArray = ArrayUtils.toObject(resultsArray);
            listResults = new LinkedList<>(Arrays.asList(doubleArray));
            Log.i("TAG", "Lisssstresults: " + listResults.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        MyMath.FilterResults(listResults);

        tvConnStatus = (TextView)findViewById(R.id.tvConnStatus);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvSentData = (TextView)findViewById(R.id.tvSentData);

        tvResult.setText(listResults.toString()); //Arrays.toString(resultsArray));

      /*  btConn.Result(tvResult);
        btConn.ListResults(listResult);
        btConn.SentData(tvSentData);

        try {
            btConn.sendData("start");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
