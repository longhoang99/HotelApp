package com.example.hotelapp.ui.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel
import com.example.hotelapp.ui.adapter.AvailableRoomListAdapter
import com.example.hotelapp.ui.adapter.ServiceListAdapter
import com.google.android.material.tabs.TabLayout
import org.json.JSONArray
import org.json.JSONObject

class ServiceListFragment : Fragment() {

    private val serviceURL = "http://10.0.2.2:8080/hotel_app/serviceList.php"
    private lateinit var recyclerView: RecyclerView
    private var adapter: ServiceListAdapter?= null
    private var serviceList: MutableList<ServiceModel> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recyclerService)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val serviceAdd: ImageView ?=  view.findViewById(R.id.img_add)
        loadServiceList()
        if(Common.currentUser?.position == "Manager") {
            serviceAdd?.setOnClickListener {
                adapter?.showAddDialog()
            }
        }else{
            serviceAdd?.visibility = INVISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.service_list, container, false)
    }

    private fun loadServiceList(){
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
                adapter = ServiceListAdapter(requireContext(), serviceList)
                recyclerView.adapter = adapter
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }){}
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}