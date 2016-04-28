package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import junit.framework.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private Button btnStartBluetooth, btnSendStart, btnStartResultActivity, test;
    private Intent bluetoothActivityIntent, resultIntent;
    private String mac_address, userName;
    private BluetoothConnection btConn;
    private TextView tvResult, tvSentData, tvConnStatus;
    private List<Float> listResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);
        drawerList.setItemChecked(position,true);
        //test
        test = (Button)findViewById(R.id.testKNOP);
        //
        Intent i = getIntent();
        userName = i.getStringExtra("userName");
        listResult = new ArrayList<Float>();
        btnStartBluetooth = (Button)findViewById(R.id.btnStartBluetooth);
        btnSendStart = (Button)findViewById(R.id.btnSendStart);
        btnStartResultActivity = (Button)findViewById(R.id.btnShowResultActivity);
        tvConnStatus = (TextView)findViewById(R.id.tvConnStatus);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvSentData = (TextView)findViewById(R.id.tvSentData);
        bluetoothActivityIntent = new Intent(this,BluetoothDeviceListActivity.class);
        resultIntent = new Intent(this, ResultsActivity.class);

        //test
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, TestActivity.class);
                startActivity(i);
            }
        });
        //

        btnStartBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(bluetoothActivityIntent);
            }
        });
        btnSendStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    btConn.sendData("start");
                    btnStartResultActivity.setVisibility(View.VISIBLE);
                    //startActivity(resultIntent);
                }
                catch (Exception e){
                    Log.e("TAG", e.toString());
                }
            }
        });
        btnStartResultActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btConn.closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle b = new Bundle();
                float[] results = btConn.getListResults();
                double[]resultsDouble = new double[results.length];
                for ( int i = 0; i < resultsDouble.length; i++ ){
                    resultsDouble[i] = MyMath.round((double) results[i], 2);
                }
                Log.i("TAG", Arrays.toString(resultsDouble));

                b.putDoubleArray("results", resultsDouble);
                Intent i = new Intent(HomeActivity.this, ResultsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            Intent i = getIntent();
            mac_address = i.getStringExtra("mac address");
            ConnectToDevice();
            //tvResult.setText(listResult.toString());
        }
        catch (Exception e){
            Log.i("TAG", e.toString());
        }
    }
    private void ConnectToDevice() {
        btConn = new BluetoothConnection();
        btConn.Result(tvResult);
        btConn.ListResults(listResult);
        btConn.SentData(tvSentData);
        tvConnStatus.setText("Connectionstatus: " + btConn.Connect(mac_address));
    }
}
