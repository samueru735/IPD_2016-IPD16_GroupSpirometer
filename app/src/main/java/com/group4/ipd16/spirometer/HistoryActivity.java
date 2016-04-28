package com.group4.ipd16.spirometer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.NumberFormat;
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
            Integer r = rand.nextInt() % 10;
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
        series.setTitle("(FVC in L/s");

        series.setThickness(5);
        series.setColor(Color.BLACK);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMinimumIntegerDigits(1);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

        //staticLabelsFormatter.setHorizontalLabels(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", " ", "SEC."});
        //staticLabelsFormatter.setVerticalLabels(new String[]{"-8", "-7", "-6", "-5", "-4", "-3", "-2", "-1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "LITER"});
        //graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setYAxisBoundsManual(true);

       // graph.getViewport().setScalable(true);

        //graph.getViewport().setScrollable(true);




        //graph.getLegendRenderer().setVisible(true);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        //StaticLabelsFormatter test = new StaticLabelsFormatter(graph);
        //test.setVerticalLabels(new String[]{"something"});
        //graph.getGridLabelRenderer().setLabelFormatter(test);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HistoryActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
