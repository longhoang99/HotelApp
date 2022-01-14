package com.example.hotelapp.ui.statistic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotelapp.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class StatisticActivity extends AppCompatActivity {
    private final String getDataURL = "http://10.0.2.2:8080/hotel_app/getData.php";
    private final String getDataUserURL = "http://10.0.2.2:8080/hotel_app/getDataUser.php";
    private final LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(new DataPoint[0]);

    private GraphView graphView;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_statistic);
        Button btnUserStatistic = findViewById(R.id.btn_statistic);
        graphView = (GraphView) findViewById(R.id.graphView);
        graphView.addSeries(dataSeries);
        graphView.getGridLabelRenderer().setNumVerticalLabels(4);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(4);
        loadData();
        btnUserStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StatisticActivity.this, UserStatistic.class);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, getDataURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray listData = new JSONArray(response);
                            DataPoint[] dataPoint = new DataPoint[listData.length()];
                            for (int i=0; i<listData.length(); i++){
                                JSONObject dataObject = listData.getJSONObject(i);
                                String xData = dataObject.getString("date");
                                String yData = dataObject.getString("totalPrice");
                                dataPoint[i] = new DataPoint(Date.valueOf(xData), Double.parseDouble(yData));
                            }
                            dataSeries.resetData(dataPoint);
                            dataSeries.setColor(Color.BLUE);
                            dataSeries.setDrawBackground(true);
                            dataSeries.setThickness(10);
                            graphView.setTitleTextSize(50);
                            graphView.setTitle("General Statistic");
                            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                                @Override
                                public String formatLabel(double value, boolean isValueX){
                                    if(isValueX){
                                        return sdf.format(new Date((long)value));
                                    }else{
                                        return super.formatLabel(value, isValueX);
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StatisticActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
        }){};
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
