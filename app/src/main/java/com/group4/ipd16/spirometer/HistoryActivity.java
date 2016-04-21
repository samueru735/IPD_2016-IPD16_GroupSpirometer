package com.group4.ipd16.spirometer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

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
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(resultDataPoints);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"0", "1", "2", "2", "3", "4", "5", "6", "7", "8", "9", "10", " ", "SEC."});
        staticLabelsFormatter.setVerticalLabels(new String[]{"-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "LITER"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        series.setTitle("(FVC, forced vital capacity) in L/s");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }
}
