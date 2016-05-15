package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ShareActivity extends BaseActivity {
    private Button btnTestMail;
    private User user;
    private double[] arrayResults;
    private double fvc, fev1;
    private Calendar cal;
    private String currentDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getLayoutInflater().inflate(R.layout.activity_share, frameLayout);
        btnTestMail = (Button)findViewById(R.id.btnTestMail);
        Intent i = getIntent();
        arrayResults = i.getDoubleArrayExtra("results");
        fvc = i.getDoubleExtra("fvc", 0.00);
        fev1 = i.getDoubleExtra("fev1", 0.00);
        currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        user = CouchbaseDB.getSpiroDB().getCurrentUser();
        sendMail();
    }

    protected void sendMail() {
        Log.i("Send email", "");

        String[] TO = {user.getDoctor_email()}; // user.getEmailDoctor
        //String[] CC = {"doctorPlastic@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        StringBuilder sbMessage = new StringBuilder();
        sbMessage.append("Results on ");
        sbMessage.append(currentDateTime);
        sbMessage.append("\n");
        sbMessage.append("\n");
        sbMessage.append("FVC: " + fvc);
        sbMessage.append("\n");
        sbMessage.append("FEV1: " + fev1);
        sbMessage.append("\n");
        sbMessage.append(Arrays.toString(arrayResults));

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Results for " + user.getFirst_name() + " " + user.getLast_name());
        emailIntent.putExtra(Intent.EXTRA_TEXT, (CharSequence) sbMessage);

        try {
            startActivity(emailIntent);
            finish();
            Log.i("TAG", "mail sent...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ShareActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
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

