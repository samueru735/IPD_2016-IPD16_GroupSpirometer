package com.group4.ipd16.spirometer;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class HistoryActivity extends BaseActivity {
    private List<Map<String, Object>> resultList = new ArrayList<>();
    private List<Double>listResults = new ArrayList<>();
    private List<Date> listViewResultDates = new ArrayList<>();
    private List<String> listViewResultStringDates = new ArrayList<>();
    private ListView lvResults;
    private ArrayAdapter<String> arrayAdapter;
    private TextView tvLegendX, tvLegendY, tvPercent;
    private Double fvc, fev1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_history);
        getLayoutInflater().inflate(R.layout.activity_history, frameLayout);
        tvLegendX = (TextView)findViewById(R.id.tvLegendX);
        tvLegendY = (TextView)findViewById(R.id.tvLegendY);
        tvPercent = (TextView)findViewById(R.id.tvPercent);
        lvResults = (ListView)findViewById(R.id.lvResults);
        drawerList.setItemChecked(position, true);
        resultList = CouchbaseDB.getSpiroDB().getResultsFromId();
        Log.i("TAG", "Resultlist from user: " + resultList);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        for(Map result : resultList){
            try {
                listViewResultDates.add(df.parse(result.get("date").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        BarGraphSeries<DataPoint> bar_series = new BarGraphSeries<DataPoint>();

        Collections.sort(listViewResultDates);
        int counter= 0;
        int dayOfMonth = 0;
        for(Date date : listViewResultDates){
            String formattedDate = df.format(date);
            String substringDate = String.format(formattedDate).substring(0, 2);
            dayOfMonth = Integer.parseInt(substringDate);
            double fvc;
            listViewResultStringDates.add(formattedDate);
            for (Map result : resultList){
                if(result.get("date").toString().equals(formattedDate)) {
                    fvc = Double.parseDouble(result.get("fvc").toString());
                    bar_series.appendData(new DataPoint(dayOfMonth, fvc),true,7);
                    counter++;
                }
            }

        }
        Log.i("TAG", "Listview dates: " + listViewResultStringDates);
        arrayAdapter = new ArrayAdapter(HistoryActivity.this,android.R.layout.simple_selectable_list_item);
        arrayAdapter.addAll(listViewResultStringDates);
        lvResults.setAdapter(arrayAdapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dateString = ((TextView) view).getText().toString();
                Log.i("TAG", "Clicked date: " + dateString);
                PutDataInListResults(dateString);
                DrawResultGraph();
            }
        });


        GraphView bar_graph = (GraphView)findViewById(R.id.graph);


        bar_graph.addSeries(bar_series);
        //kleur

        bar_series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.BLACK;
            }
        });
        //space tussen bars
        bar_series.setSpacing(5);
        // waarden op top
        bar_series.setDrawValuesOnTop(true);
        bar_series.setValuesOnTopColor(Color.BLACK);
        bar_series.setValuesOnTopSize(40);

        //scrolling
        bar_graph.getViewport().setScrollable(true);
        bar_graph.getViewport().setXAxisBoundsManual(false);
        bar_graph.getViewport().setMinX(dayOfMonth-counter);
        bar_graph.getViewport().setMaxX(dayOfMonth-counter+4);
        // tap info
        bar_series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HistoryActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PutDataInListResults(String dateString) {
        for (Map result : resultList) {
            if (result.get("date").toString().equals(dateString)) {
                listResults = (List<Double>) result.get("data");
                fvc = (Double) result.get("fvc");
                fev1 = (Double) result.get("fev1");
            }
        }
    }

    private void DrawResultGraph() {

        GraphView graph = (GraphView)findViewById(R.id.graph);
        tvLegendY.setText("FVC = " + fvc);
        tvLegendX.setText("FEV1 = " + fev1);
        tvPercent.setText("FEV1/FVC = " + MyMath.round(fev1/fvc*100, 1) + "%");
        graph.removeAllSeries();
        DataPoint[]resultDataPoints = new DataPoint[listResults.size()];
        for(int i = 0; i < listResults.size(); i++)
        {
            resultDataPoints[i] = new DataPoint(i, listResults.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(resultDataPoints);
        graph.addSeries(series);

        series.setTitle("(FVC, forced vital capacity) in L/s");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(1);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(HistoryActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
