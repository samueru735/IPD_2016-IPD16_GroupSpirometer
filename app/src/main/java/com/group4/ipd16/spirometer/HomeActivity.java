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

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button btnStartBluetooth, btnSendStart;
    private Intent bluetoothActivityIntent;
    private String mac_address;
    private BluetoothConnection btConn;
    private TextView tvResult, tvSentData, tvConnStatus;
    private List<Float> listResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listResult = new ArrayList<Float>();
        btnStartBluetooth = (Button)findViewById(R.id.btnStartBluetooth);
        btnSendStart = (Button)findViewById(R.id.btnSendStart);
        tvConnStatus = (TextView)findViewById(R.id.tvConnStatus);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvSentData = (TextView)findViewById(R.id.tvSentData);
        bluetoothActivityIntent = new Intent(this,BluetoothDeviceListActivity.class);

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
                }
                catch (Exception e){
                    Log.e("TAG", e.toString());
                }
                
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
