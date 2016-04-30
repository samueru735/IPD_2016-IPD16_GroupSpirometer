package com.group4.ipd16.spirometer;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class BluetoothDeviceListActivity extends BaseActivity {

    private TextView tvConnectionStatus;  // connection status
    private ListView lvPairedListView;    // paired bluetooth devices
    private Button btnBtSettings;         // bt settings button
    private TextView tvConnecting, tvInfoText;
    public static String MAC_ADDRESS = "mac address";

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bluetooth_device_list);
        getLayoutInflater().inflate(R.layout.activity_bluetooth_device_list, frameLayout);


        tvConnectionStatus = (TextView) findViewById(R.id.tvConnecting);
        tvConnectionStatus.setTextSize(40);

        btnBtSettings = (Button) findViewById(R.id.btnBtSettings);
        tvConnecting = (TextView) findViewById(R.id.tvConnecting);
        tvInfoText = (TextView) findViewById(R.id.tvInfoText);

        // Initialize array adapter for paired devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        lvPairedListView = (ListView) findViewById(R.id.lvPaired_devices);
        lvPairedListView.setAdapter(mPairedDevicesArrayAdapter);

        btnBtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivityForResult(settingsIntent, 1);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // check BT status to detect changes while app was paused
        checkBTState();

        mPairedDevicesArrayAdapter.clear(); // clears the array to avoid duplicated items in list

        tvConnectionStatus.setText(" "); // clear textview

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices and append to pairedDevices list
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add previously paired devices to the array
        if (pairedDevices.size() > 0) {
            findViewById(R.id.tvTitle_paired_devices).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : pairedDevices) {      // for each equivalent
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());  // name + MAC address
            }
        } else {
            mPairedDevicesArrayAdapter.add("no devices paired");
        }

        lvPairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvConnecting.setText("Attempting to connect...");
                // extract the MAC address ( = last 17 symbols)
                String deviceNameAndAddress = ((TextView) view).getText().toString();
                String macAddress = deviceNameAndAddress.substring(deviceNameAndAddress.length()-17);

                // make intent and send MAC address with it
                Intent i = new Intent(BluetoothDeviceListActivity.this, HomeActivity.class);
                i.putExtra(MAC_ADDRESS, macAddress);
                startActivity(i);
            }
        });
    }

    //method to check if the device has Bluetooth and if it is on.
    //Prompts the user to turn it on if it is off
    private void checkBTState()
    {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!mBtAdapter.isEnabled()) {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
}