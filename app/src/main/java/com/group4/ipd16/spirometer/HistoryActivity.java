package com.group4.ipd16.spirometer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
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

    public ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);
        drawerList.setItemChecked(position, true);

        list = (ListView)findViewById(R.id.listDates);

        String[] testDates = new String[]{"10 Jan 2016", "3 Okt 2015", "12 Sept 2013"};

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, testDates);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String)list.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "position" + itemPosition + "Listitem" + itemValue, Toast.LENGTH_LONG).show();

            }
        });

        GraphView bar_graph = (GraphView)findViewById(R.id.graph);
        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, -2),
                new DataPoint(1, 7),
                new DataPoint(2, 4),
                new DataPoint(3, 5),
                new DataPoint(5, 2),
                new DataPoint(4, 8)
        });
        bar_graph.addSeries(bar_series);
        //kleur

        bar_series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.BLACK;
            }
        });

        //space tussen bars
        bar_series.setSpacing(50);
        // waarden op top
        bar_series.setDrawValuesOnTop(true);
        bar_series.setValuesOnTopColor(Color.BLACK);
        bar_series.setValuesOnTopSize(40);

        //scrolling
        bar_graph.getViewport().setScrollable(true);
        bar_graph.getViewport().setXAxisBoundsManual(true);
        bar_graph.getViewport().setMinX(0.5);
        bar_graph.getViewport().setMaxX(3.5);
        bar_graph.getViewport().setScrollable(true);
        bar_graph.getViewport().scrollToEnd();
        // tap info
        bar_series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HistoryActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
