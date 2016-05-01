package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private Button btnStartBluetooth, btnSendStart, btnStartResultActivity, test;
    private Intent bluetoothActivityIntent, resultIntent;
    private String mac_address, userName;
    private BluetoothConnection btConn;
    private TextView tvResult, tvSentData, tvConnStatus, tvWelcome;
    private double incomingData, previousIncomingData;
    private List<Float> listResult;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);
        drawerList.setItemChecked(position, true);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(position);
            }
        });
        test = (Button)findViewById(R.id.testKNOP);

        Intent i = getIntent();
        userName = i.getStringExtra("userName");
        userID = i.getStringExtra("user_id");
        listResult = new ArrayList<>();
        btnStartBluetooth = (Button)findViewById(R.id.btnStartBluetooth);
        btnSendStart = (Button)findViewById(R.id.btnSendStart);
        btnStartResultActivity = (Button)findViewById(R.id.btnShowResultActivity);
        tvConnStatus = (TextView)findViewById(R.id.tvConnStatus);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvWelcome = (TextView)findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome " + userName);
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
                try {
                    btConn.sendData("start");
                    btnStartResultActivity.setVisibility(View.VISIBLE);
                    //startActivity(resultIntent);
                } catch (Exception e) {
                    Log.e("TAG", e.toString());
                }
            }
        });
        btnStartResultActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    btConn.sendData("x");
                    btConn.closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bundle b = new Bundle();
                float[] results = btConn.getListResults();
                double[] resultsDouble = new double[results.length];
                for (int i = 0; i < resultsDouble.length; i++) {
                    resultsDouble[i] = MyMath.round((double) results[i], 2);
                }
                Log.i("TAG", Arrays.toString(resultsDouble));

                b.putDoubleArray("results", resultsDouble);
                Intent i = new Intent(HomeActivity.this, ResultsActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });

        tvResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("TAG", "Incoming data: " + s);
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            //Log.i("TAG", "Mac address: " + mac_address);
           Intent i = getIntent();
            mac_address = i.getStringExtra("mac address");
            Log.i("TAG", "Mac address after intent: " + mac_address);
            ConnectToDevice();
            //tvResult.setText(listResult.toString());
        }
        catch (Exception e){
            Log.i("TAG", e.toString());
        }
    }
    private void ConnectToDevice() {

            btConn = BluetoothConnection.getInstance();
            btConn.Result(tvResult);
            btConn.ListResults(listResult);
            btConn.SentData(tvSentData);
            if(mac_address != null)
                btConn.setMacAddress(mac_address);
        tvConnStatus.setText("Connectionstatus: " + btConn.Connect());
    }

    protected void openActivity(int position){
        //drawerLayout.closeDrawers();
        drawerLayout.closeDrawer(drawerList);
        BaseActivity.position = position;

        switch (position) {
            case 0:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, ResultsActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, ShareActivity.class));
                break;
            case 5:
                Intent i = new Intent(this, ProfileActivity.class);
                i.putExtra("user_id", userID);
                startActivity(i);
                break;
            default:
                break;
        }

        Toast.makeText(this, "Selected Item Position::" + position, Toast.LENGTH_LONG).show();
    }


}
