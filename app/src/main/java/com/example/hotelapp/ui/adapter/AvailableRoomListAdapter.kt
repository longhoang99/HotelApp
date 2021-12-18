package com.example.hotelapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.CustomerModel
import com.example.hotelapp.model.RoomModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.collections.HashMap

class AvailableRoomListAdapter(private val context: Context, private val availableRoomList: MutableList<RoomModel>): RecyclerView.Adapter<AvailableRoomListAdapter.AvailableRoomViewHolder>() {

    private val bookURL = "http://10.0.2.2:8080/hotel_app/bookroom.php"
    private val editURL = "http://10.0.2.2:8080/hotel_app/editroom.php"
    private val addURL = "http://10.0.2.2:8080/hotel_app/addroom.php"
    private val deleteURL = "http://10.0.2.2:8080/hotel_app/deleteroom.php"
    class AvailableRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageRoom = itemView.findViewById(R.id.img_room) as ImageView
        var idRoom = itemView.findViewById(R.id.txt_room_id) as TextView
        var sizeRoom = itemView.findViewById(R.id.txt_room_size) as TextView
        var styleRoom = itemView.findViewById(R.id.txt_room_style) as TextView
        var statusRoom = itemView.findViewById(R.id.txt_room_status) as TextView
        var priceRoom = itemView.findViewById(R.id.txt_room_price) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableRoomViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_room, parent, false)
        return AvailableRoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return availableRoomList.size
    }

    override fun onBindViewHolder(holder: AvailableRoomViewHolder, position: Int) {
        val room = availableRoomList[position]
        holder.imageRoom.setImageDrawable(context.getDrawable(R.drawable.room_img))
        holder.idRoom.text = StringBuilder("Room "+room.id.toString())
        holder.sizeRoom.text = room.size
        holder.styleRoom.text = StringBuilder(""+room.style+" ("+room.note+")")
        holder.statusRoom.text = room.status
        holder.priceRoom.text = StringBuilder((room.price!! *1000).toString())
        val arrItem = arrayOf("Book", "Edit","Delete")
        holder.itemView.setOnClickListener{
            MaterialAlertDialogBuilder(context)
                .setItems(arrItem){_, item ->
                    when (item){
                        0 -> showBookDialog(room, position)
                        1 -> showEditDialog(room, position)
                        2 -> showConfirmDialog(position)
                    }
                }.show()
        }
    }

    private fun showBookDialog(room: RoomModel, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.customer_add_info_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Enter Customer Info")
        val bookDialog = builder.show()
        val customerModel = CustomerModel()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            bookDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            customerModel.name = dialogView.findViewById<EditText>(R.id.edt_name).text.toString()
            customerModel.birthday = dialogView.findViewById<EditText>(R.id.edt_birthday).text.toString()
            customerModel.sex = dialogView.findViewById<EditText>(R.id.edt_sex).text.toString()
            customerModel.id_number = dialogView.findViewById<EditText>(R.id.edt_id_number).text.toString()
            customerModel.phone = dialogView.findViewById<EditText>(R.id.edt_phone).text.toString()
            addToBookedList(room, customerModel, position)
            bookDialog.dismiss()
        }
    }

    private fun showConfirmDialog(position: Int){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Warning!")
        alertDialog.setMessage("Are you sure to delete?")
        alertDialog.setNegativeButton("No"){ dialogInterface, _ -> dialogInterface.dismiss()}
        alertDialog.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            deleteRoom(position)
        }
        alertDialog.show()
    }

    private fun deleteRoom(position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, deleteURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    availableRoomList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error! $response", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = availableRoomList[position].id.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun showEditDialog(room: RoomModel, position: Int){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.room_edit_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val editDialog = builder.show()
        dialogView.findViewById<TextView>(R.id.tv_room_id).text = StringBuilder("Edit Room "+availableRoomList[position].id)
        val size = dialogView.findViewById<EditText>(R.id.edt_size)
        size.setText(room.size)
        val note = dialogView.findViewById<EditText>(R.id.edt_note)
        note.setText(room.note)
        if(room.style == dialogView.findViewById<RadioButton>(R.id.rdi_double).text){
            dialogView.findViewById<RadioButton>(R.id.rdi_double).isChecked
        }
        if(room.style == dialogView.findViewById<RadioButton>(R.id.rdi_twin).text){
            dialogView.findViewById<RadioButton>(R.id.rdi_twin).isChecked
        }
        val price = dialogView.findViewById<EditText>(R.id.edt_price)
        price.setText(StringBuilder((room.price!! *1000).toString()))
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            room.size = size.text.toString()
            room.note = note.text.toString()
            val radioButton = dialogView.findViewById<RadioGroup>(R.id.rdi_group).checkedRadioButtonId
            room.style = dialogView.findViewById<RadioButton>(radioButton).text.toString()
            room.price = (price.text.toString().toDouble()/1000).toFloat()
            editRoom(room, position)
            editDialog.dismiss()
        }
    }

    private fun editRoom(roomModel: RoomModel, position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, editURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    availableRoomList[position] = roomModel
                    notifyDataSetChanged()
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error! $response", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = roomModel.id.toString()
                    params["size"] = roomModel.size.toString()
                    params["style"] = roomModel.style.toString()
                    params["note"] = roomModel.note.toString()
                    params["price"] = roomModel.price.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun addToBookedList(roomModel: RoomModel, customerModel: CustomerModel, position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, bookURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    availableRoomList.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error! $response", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["name"] = customerModel.name.toString()
                    params["birthday"] = customerModel.birthday.toString()
                    params["sex"] = customerModel.sex.toString()
                    params["id_number"] = customerModel.id_number.toString()
                    params["phone"] = customerModel.phone.toString()
                    params["room_id"] = roomModel.id.toString()
                    params["user_id"] = Common.currentUser!!.userId.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun showAddDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.room_edit_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        dialogView.findViewById<TextView>(R.id.tv_room_id).text = StringBuilder("Add New Room")
        val editDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        val roomModel = RoomModel()
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            roomModel.size = dialogView.findViewById<EditText>(R.id.edt_size).text.toString()
            roomModel.note = dialogView.findViewById<EditText>(R.id.edt_note).text.toString()
            val radioButton = dialogView.findViewById<RadioGroup>(R.id.rdi_group).checkedRadioButtonId
            roomModel.style = dialogView.findViewById<RadioButton>(radioButton).text.toString()
            val price = dialogView.findViewById<EditText>(R.id.edt_price)
            roomModel.price = (price.text.toString().toDouble()/1000).toFloat()
            addRoom(roomModel)
            editDialog.dismiss()
        }
    }

    private fun addRoom(roomModel: RoomModel) {
        val stringRequest =
            object : StringRequest(Method.POST, addURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    roomModel.id = availableRoomList[availableRoomList.lastIndex].id?.plus(1)
                    roomModel.status = "available"
                    availableRoomList.add(roomModel)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error! $response", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["size"] = roomModel.size.toString()
                    params["style"] = roomModel.style.toString()
                    params["note"] = roomModel.note.toString()
                    params["status"] = "available"
                    params["price"] = roomModel.price.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}
