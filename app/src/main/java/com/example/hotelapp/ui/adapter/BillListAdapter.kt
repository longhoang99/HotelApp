package com.example.hotelapp.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.BillModel
import com.example.hotelapp.model.UserModel

class BillListAdapter(private val context: Context, private val billList: MutableList<BillModel>): RecyclerView.Adapter<BillListAdapter.BillListViewHolder>() {

    class BillListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var stt = itemView.findViewById(R.id.txt_stt) as TextView
        var customerName = itemView.findViewById(R.id.txt_customer_name) as TextView
        var date = itemView.findViewById(R.id.txt_date) as TextView
        var totalPrice = itemView.findViewById(R.id.txt_total_price) as TextView
        var username = itemView.findViewById(R.id.txt_username) as TextView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BillListAdapter.BillListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false)
        return BillListAdapter.BillListViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillListAdapter.BillListViewHolder, position: Int) {
        val bill = billList[position]
        holder.stt.text = StringBuilder(position.plus(1).toString() + ",")
        holder.customerName.text = StringBuilder("Customer: " + bill.customerName)
        holder.date.text = StringBuilder("Date: " + (bill.date))
        holder.totalPrice.text =
            StringBuilder("total price: " + (bill.totalPrice!! * 1000).toString())
        holder.username.text = StringBuilder("username: " + bill.userName)
        holder.itemView.setOnClickListener {
            showDetailDialog(billList[position])
        }
    }

    override fun getItemCount(): Int {
        return billList.size
    }
    private fun showDetailDialog(bill: BillModel) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.bill_detail_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val infoDialog = builder.show()
        dialogView.findViewById<TextView>(R.id.txt_customer_name).setText(StringBuilder("Customer: "+ bill.customerName))
        dialogView.findViewById<TextView>(R.id.txt_customer_birthday).setText(StringBuilder("Birthday: "+bill.customerBirthday))
        dialogView.findViewById<TextView>(R.id.txt_customer_sex).setText(StringBuilder("Sex: "+bill.customerSex))
        dialogView.findViewById<TextView>(R.id.txt_customer_id_number).setText(StringBuilder("ID number: "+bill.customerIdNumber))
        dialogView.findViewById<TextView>(R.id.txt_customer_phone).setText(StringBuilder("Phone: "+bill.customerPhone))
        dialogView.findViewById<TextView>(R.id.txt_room_id).setText(StringBuilder("Room: "+bill.roomId.toString()))
        dialogView.findViewById<TextView>(R.id.txt_room_size).setText(StringBuilder("Size: "+bill.roomSize))
        dialogView.findViewById<TextView>(R.id.txt_room_style).setText(StringBuilder("Style: "+bill.roomStyle))
        dialogView.findViewById<TextView>(R.id.txt_room_note).setText(StringBuilder("Note: "+bill.roomNote))
        dialogView.findViewById<TextView>(R.id.txt_service_name).setText(StringBuilder("Service: "+bill.serviceName))
        dialogView.findViewById<TextView>(R.id.txt_date).setText(StringBuilder("Date: "+bill.date))
        dialogView.findViewById<TextView>(R.id.txt_total_price).setText(StringBuilder("Total: "+(bill.totalPrice!! * 1000).toString()))
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            infoDialog.dismiss()
        }
    }

}