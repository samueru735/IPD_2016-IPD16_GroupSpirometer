package com.group4.ipd16.spirometer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;


public class HistoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);
        drawerList.setItemChecked(position, true);

        SetupGraph();

    }
private int n = 20;
    private void SetupGraph() {
        ArrayList<Integer> arrayRandom = new ArrayList<Integer>(n);
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i=0; i<n; i++)
        {
            Integer r = rand.nextInt() % 256;
            arrayRandom.add(r);

        }
        DrawGraph(arrayRandom);
    }
    private void DrawGraph(ArrayList<Integer> listResults) {
        // init example series data
        GraphView graph = (GraphView)findViewById(R.id.graphHistory);

        DataPoint[]resultDataPoints = new DataPoint[listResults.size()];
        for(int i = 0; i < listResults.size(); i++)
        {
            resultDataPoints[i] = new DataPoint(i, listResults.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(resultDataPoints);
        graph.addSeries(series);

    }
}
