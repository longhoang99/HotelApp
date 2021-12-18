package com.example.hotelapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.model.CustomerModel
import com.example.hotelapp.model.RoomModel
import org.json.JSONArray
import org.json.JSONObject

class BookedRoomListAdapter(private val context: Context, private val BookedRoomList: MutableList<RoomModel>): RecyclerView.Adapter<BookedRoomListAdapter.BookedRoomViewHolder>() {

    private val getInfoURL = "http://10.0.2.2:8080/hotel_app/customerInfo.php"
    class BookedRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageRoom = itemView.findViewById(R.id.img_room) as ImageView
        var idRoom = itemView.findViewById(R.id.txt_room_id) as TextView
        var sizeRoom = itemView.findViewById(R.id.txt_room_size) as TextView
        var styleRoom = itemView.findViewById(R.id.txt_room_style) as TextView
        var statusRoom = itemView.findViewById(R.id.txt_room_status) as TextView
        var priceRoom = itemView.findViewById(R.id.txt_room_price) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookedRoomViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        return BookedRoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return BookedRoomList.size
    }

    override fun onBindViewHolder(holder: BookedRoomViewHolder, position: Int) {
        val room = BookedRoomList[position]
        holder.imageRoom.setImageDrawable(context.getDrawable(R.drawable.room_img))
        holder.idRoom.text = StringBuilder("Room "+room.id.toString())
        holder.sizeRoom.text = room.size
        holder.styleRoom.text = StringBuilder(""+room.style+" ("+room.note+")")
        holder.statusRoom.text = room.status
        holder.priceRoom.text = StringBuilder((room.price!! *1000).toString())
        holder.itemView.setOnClickListener{
            showRoomDetail(room)
        }
    }

    private fun showRoomDetail(roomModel: RoomModel) {
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
        dialogView.findViewById<TextView>(R.id.txt_room_service).visibility = INVISIBLE
        dialogView.findViewById<TextView>(R.id.txt_service_name).visibility = INVISIBLE
        dialogView.findViewById<TextView>(R.id.txt_service_price).visibility = INVISIBLE
        val infoDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            infoDialog.dismiss()
        }
    }


}