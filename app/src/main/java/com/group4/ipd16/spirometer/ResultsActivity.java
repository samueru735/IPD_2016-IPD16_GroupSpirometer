package com.group4.ipd16.spirometer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends BaseActivity {
    BluetoothConnection btConn;
    Intent intent;
    private TextView tvResult, tvSentData, tvConnStatus;
    private double[] resultsArray;
    private double fvc, fev1;
    private List<Double> listResults = new ArrayList<Double>();
    private Button btnSendMail, btnSaveResults, btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_results);
        getLayoutInflater().inflate(R.layout.activity_results, frameLayout);

        Bundle b = this.getIntent().getExtras();
        try{
            resultsArray =  b.getDoubleArray("results");
            Double[] doubleArray = ArrayUtils.toObject(resultsArray);
            listResults = new LinkedList<>(Arrays.asList(doubleArray));
            Log.i("TAG", "Lisssstresults: " + listResults.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i("TAG", "Current user in results: " + CouchbaseDB.getSpiroDB().getCurrentUser().getFirst_name());


        tvConnStatus = (TextView)findViewById(R.id.tvConnStatus);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvSentData = (TextView)findViewById(R.id.tvSentData);
        btnSendMail = (Button)findViewById(R.id.btnSendMail);
        btnSaveResults = (Button) findViewById(R.id.btnSaveResults);
        btnRetry = (Button)findViewById(R.id.btnRetry);

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultsActivity.this, ShareActivity.class);
                i.putExtra("results", ArrayUtils.toDoubleArray(listResults));
                i.putExtra("fvc", fvc);
                i.putExtra("fev1", fev1);
                startActivity(i);
            }
        });
        btnSaveResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveResults();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoBackToHome();
            }
        });

        listResults = MyMath.FilterPeakResults(listResults);
        DrawGraph();
        listResults = MyMath.FilterZeroResults(listResults);
        listResults = MyMath.FilterExpiration(listResults);
        fev1 = MyMath.FEV1(listResults);
        fev1 = MyMath.round(fev1,2);
        fvc = MyMath.FVC(listResults);
        fvc = MyMath.round(fvc,2);
        tvResult.setText("FVC = " + fvc + "FEV1 = " + fev1); //Arrays.toString(resultsArray));
    }

    private void GoBackToHome() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    private void SaveResults() {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Calendar c = Calendar.getInstance();
        Log.i("TAG","Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        data.put("date", formattedDate);
        data.put("data", listResults);
        data.put("fvc", fvc);
        data.put("fev1", fev1);
        data.put("user_id", CouchbaseDB.getSpiroDB().getUserID());

        //resultMap.put("res_id_" + DateFormat.getDateTimeInstance().format(new Date()), data);
        //resultMap.put("result", data);
        try{
            //CouchbaseDB.getSpiroDB().updateResults(CouchbaseDB.getSpiroDB().getUserID(), resultMap);
            CouchbaseDB.getSpiroDB().CreateDocument(data);
            Toast.makeText(ResultsActivity.this, "Results saved", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Log.e("TAG", "Error saving results", e);
        }

    }

    private void DrawGraph() {
        // init example series data
        GraphView graph = (GraphView)findViewById(R.id.graphResults);

        DataPoint[]resultDataPoints = new DataPoint[listResults.size()];
        for(int i = 0; i < listResults.size(); i++)
        {
            resultDataPoints[i] = new DataPoint(i, listResults.get(i));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(resultDataPoints);
        graph.addSeries(series);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
       // staticLabelsFormatter.setHorizontalLabels(new String[]{"0", "1", "2", "2", "3", "4", "5", "6", "7", "8", "9", "10", " ", "SEC."});
       // staticLabelsFormatter.setVerticalLabels(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "LITER"});
       // graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setTitle("(FVC, forced vital capacity) in L/s");

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(1);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(ResultsActivity.this, "Series: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
