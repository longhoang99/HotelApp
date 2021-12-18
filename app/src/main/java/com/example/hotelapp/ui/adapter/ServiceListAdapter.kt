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
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel

class ServiceListAdapter(private val context: Context, private val serviceList: MutableList<ServiceModel>): RecyclerView.Adapter<ServiceListAdapter.ServiceListViewHolder>() {

    private val addURL = "http://10.0.2.2:8080/hotel_app/addService.php"
    private val editURL = "http://10.0.2.2:8080/hotel_app/editService.php"
    private val deleteURL = "http://10.0.2.2:8080/hotel_app/deleteService.php"
    class ServiceListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var serviceId = itemView.findViewById(R.id.txt_service_id) as TextView
        var serviceName = itemView.findViewById(R.id.txt_service_name) as TextView
        var servicePrice = itemView.findViewById(R.id.txt_service_price) as TextView
        var serviceEdit: ImageView ?= itemView.findViewById(R.id.img_edit_service)
        var serviceDelete: ImageView ?= itemView.findViewById(R.id.img_delete_service)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_service, parent, false)
        return ServiceListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceListViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceId.text = StringBuilder(position.plus(1).toString()+",")
        holder.serviceName.text = service.name
        holder.servicePrice.text = StringBuilder((service.price!! *1000).toString())

        holder.serviceEdit?.setOnClickListener {
            showEditDialog(service, position)
        }
        holder.serviceDelete?.setOnClickListener {
            showConfirmDialog(position)
        }
    }

    fun showAddDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.service_edit_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit Service")
        val editDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        val serviceModel= ServiceModel()
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            serviceModel.name = dialogView.findViewById<EditText>(R.id.edt_serviceName).text.toString()
            val price = dialogView.findViewById<EditText>(R.id.edt_servicePrice).text.toString()
            serviceModel.price = (price.toDouble()/1000).toFloat()
            addService(serviceModel)
            editDialog.dismiss()
        }
    }

    private fun addService(serviceModel: ServiceModel) {
        val stringRequest =
            object : StringRequest(Method.POST, addURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    serviceModel.serviceId = serviceList.lastIndex+1
                    serviceList.add(serviceModel)
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
                    params["name"] = serviceModel.name.toString()
                    params["price"] = serviceModel.price.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun showEditDialog(serviceModel: ServiceModel, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.service_edit_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit Service")
        val editDialog = builder.show()
        val edtName = dialogView.findViewById<EditText>(R.id.edt_serviceName)
        val edtPrice = dialogView.findViewById<EditText>(R.id.edt_servicePrice)
        edtName.setText(serviceModel.name)
        edtPrice.setText(StringBuilder((serviceModel.price!! *1000).toString()))
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            serviceModel.name = edtName.text.toString()
            serviceModel.price = (edtPrice.text.toString().toDouble()/1000).toFloat()
            editService(serviceModel, position)
            editDialog.dismiss()
        }
    }

    private fun editService(serviceModel: ServiceModel, position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, editURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    serviceList[position] = serviceModel
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
                    params["id"] = serviceModel.serviceId.toString()
                    params["name"] = serviceModel.name.toString()
                    params["price"] = serviceModel.price.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    private fun showConfirmDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Warning!")
        alertDialog.setMessage("Are you sure to delete?")
        alertDialog.setNegativeButton("No"){ dialogInterface, _ -> dialogInterface.dismiss()}
        alertDialog.setPositiveButton("Yes") { dialogInterface, _ ->
            dialogInterface.dismiss()
            deleteService(position)
        }
        alertDialog.show()
    }

    private fun deleteService(position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, deleteURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    serviceList.removeAt(position)
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
                    params["id"] = serviceList[position].serviceId.toString()
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