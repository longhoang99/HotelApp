package com.example.hotelapp.ui.bill

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.BillModel
import com.example.hotelapp.model.UserModel
import com.example.hotelapp.ui.adapter.BillListAdapter
import com.example.hotelapp.ui.adapter.UserListAdapter
import org.json.JSONArray
import org.json.JSONObject

class BillListFragment : Fragment() {

    private val billListURL = "http://10.0.2.2:8080/hotel_app/getBillList.php"
    private lateinit var recyclerView: RecyclerView
    private var adapter: BillListAdapter?= null
    private var billList: MutableList<BillModel> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerBill)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadBillList()
    }

    private fun loadBillList() {
        val stringRequest =
            object : StringRequest(billListURL, Response.Listener { response:String ->
                val billArr: JSONArray = JSONArray(response)
                for (i: Int in 0 until billArr.length())
                {
                    val billObject: JSONObject = billArr.getJSONObject(i)
                    val bill= BillModel()
                    bill.userName = billObject.getString("username")
                    bill.positionId = Integer.parseInt(billObject.getString("positionId"))
                    bill.customerName = billObject.getString("customerName")
                    bill.customerBirthday= billObject.getString("customerBirthday")
                    bill.customerSex = billObject.getString("customerSex")
                    bill.customerIdNumber = billObject.getString("customerIdNumber")
                    bill.customerPhone = billObject.getString("customerPhone")
                    bill.roomId = Integer.parseInt(billObject.getString("roomId"))
                    bill.roomSize = billObject.getString("roomSize")
                    bill.roomStyle = billObject.getString("roomStyle")
                    bill.roomNote= billObject.getString("roomNote")
                    bill.serviceName = billObject.getString("serviceName")
                    bill.date = billObject.getString("date")
                    bill.totalPrice = billObject.getString("totalPrice").toDouble().toFloat()
                    billList.add(bill)
                }
                val staffBill : MutableList<BillModel> = ArrayList()
                adapter = if (Common.currentUser!!.position != "Manager"){
                    for (i: Int in 0 until billList.size){
                        if(billList[i].userName == Common.currentUser!!.username){
                            staffBill.add(billList[i])
                        }
                    }
                    BillListAdapter(requireContext(), staffBill)
                }else
                    BillListAdapter(requireContext(), billList)
                recyclerView.adapter = adapter
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }){}
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bill_list, container, false)
    }


}