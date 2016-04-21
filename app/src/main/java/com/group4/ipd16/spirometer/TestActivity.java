package com.group4.ipd16.spirometer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TestActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    private int progressStatus =0;
    private Handler handler = new Handler();
    private Button start;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test);
        getLayoutInflater().inflate(R.layout.activity_test, frameLayout);

        progressBar = (ProgressBar)findViewById(R.id.myProgressBar);
        progressText = (TextView)findViewById(R.id.myProgressText);
        start = (Button)findViewById(R.id.knopSTART);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressStatus = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressStatus < 100) {
                            progressStatus += 1;
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                    progressText.setText("Progress: " + progressStatus + " / " + progressBar.getMax());
                                }
                            });
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });

    }

}
