package com.example.hotelapp.ui.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.MainActivity
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.databinding.ActivityPayBinding
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Pay_Activity : AppCompatActivity() {
    private val setRoomAvailableURL = "http://10.0.2.2:8080/hotel_app/setRoomAvailable.php"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        val customerName = intent.getStringExtra("name")
        val roomId = intent.getStringExtra("roomId")
        val serviceName = intent.getStringExtra("serviceName")
        val totalPrice = intent.getStringExtra("totalPrice")
        findViewById<TextView>(R.id.txt_date).text = StringBuilder("Date: ").append(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
        findViewById<TextView>(R.id.txt_customer_name).text = StringBuilder("Customer Name: ").append(customerName)
        findViewById<TextView>(R.id.txt_room).text = StringBuilder("Room: ").append(roomId)
        findViewById<TextView>(R.id.txt_service).text = StringBuilder("Service: ").append(serviceName)
        findViewById<TextView>(R.id.txt_total_price).text = StringBuilder("Total: ").append(totalPrice)
        findViewById<TextView>(R.id.txt_staff).text = StringBuilder("Cashier: ").append(Common.currentUser?.username)
        findViewById<Button>(R.id.btn_pay).setOnClickListener {
            val stringRequest =
                object : StringRequest(Method.POST, setRoomAvailableURL, Response.Listener{ response: String ->
                    if (response == "Success") {
                        Toast.makeText(this, "Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error! $response", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error: VolleyError? ->
                    Toast.makeText(this, "" + error!!.message, Toast.LENGTH_SHORT).show()
                }) {

                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["id"] = roomId.toString()
                        return params
                    }
                }
            val requestQueue: RequestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)
        }


    }
}