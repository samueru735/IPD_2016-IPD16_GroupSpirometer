package com.group4.ipd16.spirometer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnection{

    private TextView myLabel;
    private EditText myTextbox;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    private Thread workerThread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private int counter;        // can be used later
    private volatile boolean stopWorker;
    //private String mac_address;
    private Intent intent;
    private TextView tvConnectInfo;
    private TextView result, sentdata;
    private List<Float> listResults;

    private static BluetoothConnection mIstance = null;

    private String mac_address;

    private BluetoothConnection(){
        mac_address = "none";
    }

    public void setMacAddress(String value){
        mac_address = value;
    }

    public static BluetoothConnection getInstance(){
        if(mIstance == null)
            mIstance = new BluetoothConnection();
        return mIstance;
    }


    public void SentData(TextView sentdata) {
        this.sentdata = sentdata;
    }
    public void Result(TextView resultBox) {
        this.result = resultBox;
    }
    public void ListResults(List<Float> listResults){
        this.listResults = listResults;
        Log.i("TAG", "listresults: "+ listResults.toString());
    }

    public float[] getListResults(){
        float[] results = new float[listResults.size()];
        int i = 0;
        for (Float f : listResults) {
            results[i++] = (f != null ? f : 0);
        }
        Log.i("TAG", "bt_results " + Arrays.toString(results));
        return results;
    }

    public String Connect(){//String macaddress){
        //this.mac_address = macaddress;
        Log.i("TAG", mac_address);
        try
        {
            findBT();
            openBT();
            return "Connected";
        }
        catch (Exception ex) {
            Log.e("TAG", "Connection to " + mmDevice.getName() + " at "
                    + mmDevice.getAddress() + " failed:" + ex.getMessage());
            }
        if(!mac_address.equals("none"))
            return "Still connected";
        else
            return "couldn't connect";
    }

    private void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                //if(device.getName().equals("spiroSensor"))
                if(device.getAddress().equals(mac_address))
                {
                    mmDevice = device; Log.i("TAG","gotcha");
                    break;
                }
            }
        }
    }

    void openBT() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Log.i("TAG", "openBT");
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        Log.i("TAG", "before");
        mmSocket.connect();
        Log.i("TAG", "connected");
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();
    }

    void beginListenForData()
    {
       final Handler handler = new Handler();
        final byte delimiter = 10; // ASCII code for a newline character
        Log.i("TAG", "start listening");
        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            try {
                                                Log.i("TAG", "trying");
                                                listResults.add(Float.parseFloat(data.toString()));
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            //String dataString = data.toString();
                                           // String testData = dataString.trim();
                                            result.setText(listResults.toString());
                                            Log.i("TAG", "data received");
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    void sendData() throws IOException
    {
        String msg = myTextbox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        sentdata.setText("data sent: " + msg);
    }
    void sendData(String command) throws IOException
    {
        String msg = command;
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        sentdata.setText("data sent: " + msg);
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        if(mmOutputStream != null)
            mmOutputStream.close();
        if(mmInputStream != null)
            mmInputStream.close();
        if(mmSocket != null)
            mmSocket.close();
        Log.i("TAG","Bluetooth Closed");
    }

}
