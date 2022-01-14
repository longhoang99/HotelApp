package com.example.hotelapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel
import com.example.hotelapp.ui.service.Pay_Activity
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class RoomServiceAdapter(private val context: Context, private val bookedList: MutableList<RoomModel>): RecyclerView.Adapter<RoomServiceAdapter.BookedListViewHolder>() {

    private val updateOrderURL = "http://10.0.2.2:8080/hotel_app/updateOrder.php"
    private val roomServiceURL = "http://10.0.2.2:8080/hotel_app/roomServiceInfo.php"
    private val getInfoURL = "http://10.0.2.2:8080/hotel_app/customerInfo.php"
    private val serviceURL = "http://10.0.2.2:8080/hotel_app/serviceList.php"
    private lateinit var recyclerViewServiceList: RecyclerView
    private var adapterServiceList: ServiceToAddAdapter?= null
    private var serviceList: MutableList<ServiceModel> = ArrayList()
    class BookedListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageRoom = itemView.findViewById(R.id.img_room) as ImageView
        var idRoom = itemView.findViewById(R.id.txt_room_id) as TextView
        var sizeRoom = itemView.findViewById(R.id.txt_room_size) as TextView
        var styleRoom = itemView.findViewById(R.id.txt_room_style) as TextView
        var statusRoom = itemView.findViewById(R.id.txt_room_status) as TextView
        var priceRoom = itemView.findViewById(R.id.txt_room_price) as TextView
        var serviceAdd: ImageView ?= itemView.findViewById(R.id.img_add_service)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_room_service, parent, false)
        return BookedListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookedList.size
    }

    override fun onBindViewHolder(holder: BookedListViewHolder, position: Int) {
        val room = bookedList[position]
        holder.imageRoom.setImageDrawable(context.getDrawable(R.drawable.room_img))
        holder.idRoom.text = StringBuilder("Room "+room.id.toString())
        holder.sizeRoom.text = room.size
        holder.styleRoom.text = StringBuilder(""+room.style+" ("+room.note+")")
        holder.statusRoom.text = room.status
        holder.priceRoom.text = StringBuilder((room.price!! *1000).toString())
        holder.itemView.setOnClickListener{
            showDetail(room)
        }
        holder.serviceAdd?.setOnClickListener {
            showAddDialog(room)
        }
    }

    private fun showAddDialog(room: RoomModel) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(R.layout.room_service_bottom_sheet,(ConstraintLayout(context)).findViewById(R.id.bottomSheetContainer))
        recyclerViewServiceList = bottomSheetView.findViewById(R.id.recyclerBottomSheet)
        recyclerViewServiceList.setHasFixedSize(true)
        recyclerViewServiceList.layoutManager = LinearLayoutManager(context)
        loadServiceList(room)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showDetail(roomModel: RoomModel) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.room_info_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val stringRequest =
            object : StringRequest(Method.POST, getInfoURL, Response.Listener{ response: String ->
                val customerInfo: JSONArray = JSONArray(response)
                val customer: JSONObject = customerInfo.getJSONObject(0)
                dialogView.findViewById<TextView>(R.id.txt_customer_name).text = StringBuilder("Name: "+customer.getString("name"))
                dialogView.findViewById<TextView>(R.id.txt_customer_birthday).text = StringBuilder("Birthday: "+customer.getString("birthday"))
                dialogView.findViewById<TextView>(R.id.txt_customer_sex).text = StringBuilder("Sex: "+customer.getString("sex"))
                dialogView.findViewById<TextView>(R.id.txt_customer_id_number).text = StringBuilder("ID number: "+customer.getString("idNumber"))
                dialogView.findViewById<TextView>(R.id.txt_customer_phone).text = StringBuilder("Phone: "+customer.getString("phone"))
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = roomModel.id.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
        val serviceName = dialogView.findViewById<TextView>(R.id.txt_service_name)
        val servicePrice = dialogView.findViewById<TextView>(R.id.txt_service_price)
        val stringRequest2 =
            object : StringRequest(Method.POST, roomServiceURL, Response.Listener { response: String ->
                val roomServiceInfo: JSONArray = JSONArray(response)
                var name: String = ""
                var price: Float = 0.0F
                for(i: Int in 0 until roomServiceInfo.length()){
                    name += roomServiceInfo.getJSONObject(i).getString("service_name").plus(",")
                    price += roomServiceInfo.getJSONObject(i).getString("service_price").toDouble().toFloat()
                }
                serviceName.text = StringBuilder("Service Name: $name")
                servicePrice.text = StringBuilder("Total Price: "+(price*1000).toString()+"vnd")
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = roomModel.id.toString()
                    return params
                }
            }
        val requestQueue2: RequestQueue = Volley.newRequestQueue(context)
        requestQueue2.add(stringRequest2)
        val infoDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            infoDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_pay).setOnClickListener {
            val stringRequest3 =
                object : StringRequest(Method.POST, updateOrderURL, Response.Listener { response: String ->
                    val info: JSONArray = JSONArray(response)
                    var name: String=""
                    var roomPrice: Float = 0.0F
                    var serviceName: String=""
                    var servicePrice: Float=0.0F
                    for(i: Int in 0 until info.length()){
                        name = info.getJSONObject(i).getString("name")
                        roomPrice = info.getJSONObject(i).getString("roomPrice").toDouble().toFloat()
                        serviceName += info.getJSONObject(i).getString("serviceName").plus(",")
                        servicePrice += info.getJSONObject(i).getString("servicePrice").toDouble().toFloat()
                    }
                    val intent= Intent(context, Pay_Activity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("roomId", roomModel.id.toString())
                    intent.putExtra("serviceName", serviceName)
                    intent.putExtra("totalPrice", (roomPrice+servicePrice).toString())
                    context.startActivity(intent)
                }, Response.ErrorListener { error: VolleyError? ->
                    Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
                }) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["id"] = roomModel.id.toString()
                        return params
                    }
                }
            val requestQueue3: RequestQueue = Volley.newRequestQueue(context)
            requestQueue3.add(stringRequest3)
            infoDialog.dismiss()
        }
    }

    private fun loadServiceList(room: RoomModel){
        val stringRequest =
            object : StringRequest(serviceURL, Response.Listener { response:String ->
                val serviceArr: JSONArray = JSONArray(response)
                for (i: Int in 0 until serviceArr.length())
                {
                    val serviceObject: JSONObject = serviceArr.getJSONObject(i)
                    val service= ServiceModel()
                    service.serviceId = Integer.parseInt(serviceObject.getString("id"));
                    service.name = serviceObject.getString("name")
                    service.price= serviceObject.getString("price").toDouble().toFloat()
                    serviceList.add(service)
                }
                adapterServiceList = ServiceToAddAdapter(context, serviceList, room)
                recyclerViewServiceList.adapter = adapterServiceList
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }){}
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}