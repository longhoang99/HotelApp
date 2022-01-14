package com.example.hotelapp.ui.statistic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.databinding.FragmentGeneralBinding
import com.example.hotelapp.model.RoomModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import org.json.JSONArray
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class GeneralFragment : Fragment() {

    private val getDataURL = "http://10.0.2.2:8080/hotel_app/getData.php"
    private var lineList: ArrayList<Entry> = ArrayList()
    private lateinit var lineData: LineData
    private lateinit var lineDataSet: LineDataSet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    private fun loadData() {
        val stringRequest =
            object : StringRequest(Method.GET, getDataURL, Response.Listener{ response: String ->
                Log.d("aaaa", ""+response)
                val listData: JSONArray = JSONArray(response)
                for (i: Int in 0 until listData.length()) {
                    val dataObject: JSONObject = listData.getJSONObject(i)
                    val xData = dataObject.getString("date")
                    val yData = dataObject.getString("totalPrice")
                    lineList.add(Entry(xData.toDouble().toFloat(),yData.toDouble().toFloat()))
                }
                lineDataSet = LineDataSet(lineList, "Date")
                lineData = LineData(lineDataSet)
                requireView().findViewById<LineChart>(R.id.lineChart).data = lineData
                lineDataSet.color = Color.BLACK
                //lineDataSet.setColors(*ColorTemplate.JOYFUL_COLORS)
                lineDataSet.valueTextColor = Color.BLUE
                lineDataSet.valueTextSize = 40f
                lineDataSet.setDrawFilled(true)
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {}
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

}