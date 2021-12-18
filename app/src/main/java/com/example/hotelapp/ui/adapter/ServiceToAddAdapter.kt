package com.example.hotelapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel
import org.json.JSONArray
import org.json.JSONObject

class ServiceToAddAdapter(private val context: Context, private val serviceList: MutableList<ServiceModel>, roomModel: RoomModel): RecyclerView.Adapter<ServiceToAddAdapter.ServiceToAddViewHolder>() {

    private val roomId = roomModel.id
    private val addServiceToRoomURL = "http://10.0.2.2:8080/hotel_app/addServiceToRoom.php"
    class ServiceToAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var serviceId = itemView.findViewById(R.id.txt_service_id) as TextView
        var serviceName = itemView.findViewById(R.id.txt_service_name) as TextView
        var servicePrice = itemView.findViewById(R.id.txt_service_price) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceToAddViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_service_add, parent, false)
        return ServiceToAddViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceToAddViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceId.text = StringBuilder(position.plus(1).toString()+",")
        holder.serviceName.text = service.name
        holder.servicePrice.text = StringBuilder((service.price!! *1000).toString())
        holder.itemView.setOnClickListener {
            addServicetoRoom(service, roomId)
        }
    }

    fun addServicetoRoom(service: ServiceModel, roomId: Int?) {
        val stringRequest =
            object : StringRequest(Method.POST, addServiceToRoomURL, Response.Listener{ response: String ->
                if(response == "Success"){
                    Toast.makeText(context, "Add Service Success",Toast.LENGTH_SHORT).show()
                }else
                    Toast.makeText(context, "Error: $response", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["room_id"] = roomId.toString()
                    params["service_id"] = service.serviceId.toString()
                    params["user_id"] = Common.currentUser?.userId.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}