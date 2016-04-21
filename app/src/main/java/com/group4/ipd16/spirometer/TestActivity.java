package com.group4.ipd16.spirometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class TestActivity extends BaseActivity {

    protected static final int TIMER_RUNTIME = 10000;
    protected boolean mbActive;
    protected ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test);
        getLayoutInflater().inflate(R.layout.activity_test, frameLayout);


        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        final Thread timerThread = new Thread(){
            @Override
            public void run() {
                mbActive = true;
                try{
                    int waited = 0;
                    while(mbActive && (waited < TIMER_RUNTIME)){
                        sleep(200);
                        if(mbActive){
                            waited += 200;
                            updateProgress(waited);
                            Log.d("GO", "LOADING");
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("FAIL", "FAIL");
                }finally {
                    onContinue();
                }
            }
        };
        timerThread.start();
    }

    public void  updateProgress(final int timePassed){
        if(null != mProgressBar){
            final int progress = mProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            mProgressBar.setProgress(progress);
        }
    }

    public void onContinue(){
        Log.d("messageFinal", "Loading bar just loaded");
    }
}
