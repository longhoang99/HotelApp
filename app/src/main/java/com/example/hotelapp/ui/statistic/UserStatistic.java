package com.example.hotelapp.ui.statistic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserStatistic extends AppCompatActivity {

    private final String getDataUserURL = "http://10.0.2.2:8080/hotel_app/getDataUser.php";
    private BarChart barChart;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.user_statistic);
        barChart = findViewById(R.id.barChart);
        loadData();
    }

    private void loadData() {
            StringRequest stringRequest =
                    new StringRequest(Request.Method.GET, getDataUserURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray listData = new JSONArray(response);
                                ArrayList<BarEntry> barEntries = new ArrayList<>();
                                ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                                ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                                String user1 = null;
                                String user2 = null;
                                String user3 = null;
                                for (int i=0; i<listData.length(); i++){
                                    JSONObject dataObject = listData.getJSONObject(i);
                                    String userData = dataObject.getString("userName");
                                    String xData = dataObject.getString("date");
                                    String yData = dataObject.getString("totalPrice");
                                    if(user1 == null || user1.equals(userData)){
                                        user1 = userData;
                                        barEntries.add(new BarEntry(Date.valueOf(xData).getMonth(), Float.parseFloat(yData)));
                                    } else if(user2 == null|| user2.equals(userData)){
                                        user2 = userData;
                                        barEntries2.add(new BarEntry(Date.valueOf(xData).getMonth(), Float.parseFloat(yData)));
                                    } else if(user3 == null|| user3.equals(userData)){
                                        user3 = userData;
                                        barEntries3.add(new BarEntry(Date.valueOf(xData).getMonth(), Float.parseFloat(yData)));
                                    }
                                }
                                BarDataSet barDataSet1 = new BarDataSet(barEntries, user1);
                                barDataSet1.setColor(Color.RED);
                                barDataSet1.setValueTextSize(15);
                                BarDataSet barDataSet2 = new BarDataSet(barEntries2, user2);
                                barDataSet2.setColor(Color.BLUE);
                                barDataSet2.setValueTextSize(15);
                                BarDataSet barDataSet3 = new BarDataSet(barEntries3, user3);
                                barDataSet3.setColor(Color.MAGENTA);
                                barDataSet3.setValueTextSize(15);
                                BarData data = new BarData(barDataSet1, barDataSet2,barDataSet3);
                                barChart.setData(data);
                                String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                XAxis xAxis = barChart.getXAxis();
                                xAxis.setTextSize(15);
//                                xAxis.setValueFormatter(new IAxisValueFormatter() {
//                                    @Override
//                                    public String getFormattedValue(float v, AxisBase axisBase) {
//                                        return "Month "+data.getXMax();
//                                    }
//                                });
                                YAxis yAxis = barChart.getAxisLeft();
                                yAxis.setTextSize(20);
                                barChart.getAxisRight().setTextSize(20);
                                xAxis.setCenterAxisLabels(true);
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setGranularity(1);
                                xAxis.setGranularityEnabled(true);
                                barChart.setDragEnabled(true);
                                barChart.setVisibleXRangeMaximum(3);
                                barChart.setFitBars(true);
                                float barSpace = 0.08f;
                                float groupSpace = 0.44f;
                                data.setBarWidth(0.10f);
                                barChart.getXAxis().setAxisMinimum(0);
                                barChart.getXAxis().setAxisMaximum(12);
                                barChart.getAxisLeft().setAxisMinimum(data.getYMin()-100);
                                barChart.groupBars(0, groupSpace,barSpace);
                                barChart.invalidate();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserStatistic.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    }){};
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

}
