package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Samuel on 5/04/2016.
 */
public class Mail extends AppCompatActivity {
    String message;

    public void Message(String message){
        message = this.message;
    }
    public void send() {
        Log.i("Send email", "");

        String[] TO = {"samcontrol@hotmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("TAG", "Finished sending");
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }

    }
}
