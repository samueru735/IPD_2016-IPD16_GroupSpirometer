package com.group4.ipd16.spirometer;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bob on 16/05/16.
 */
public class CSVHandler {
    private double time;
    private String TAG;
    StringBuilder sb;
    public CSVHandler() {
        time = 0.0;
    }

    public void CreateCSVFile(double[] data){
        time = 0.0;
        sb = new StringBuilder();
        sb.append("Time(ms)");
        sb.append(",");
        sb.append("FVC(litres)");
        sb.append(System.getProperty("line.separator"));

        for (double element:data) {
            sb.append(time);
            sb.append(",");
            sb.append(element);
            sb.append(System.getProperty("line.separator"));
            time += 10;
        }
        StoreCSV();
    }
    private void StoreCSV(){
        File csvFile = getOutputMediaFile();
        if (csvFile == null) {
            Log.d(TAG,"Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(csvFile));
            br.write(sb.toString());
            br.close();
            br.flush();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + BaseActivity.PACKAGE_NAME
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="Results_"+ timeStamp +".csv";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
