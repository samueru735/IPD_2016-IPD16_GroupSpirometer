package com.group4.ipd16.spirometer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class HistoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);
    }
}
