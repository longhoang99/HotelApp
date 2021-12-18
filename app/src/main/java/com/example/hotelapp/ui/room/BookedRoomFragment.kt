package com.example.hotelapp.ui.room

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.ui.adapter.BookedRoomListAdapter
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class BookedRoomFragment : Fragment(){
    private val roomURL = "http://10.0.2.2:8080/hotel_app/roomlist.php"
    private lateinit var recyclerView: RecyclerView
    private var adapter: BookedRoomListAdapter?= null
    private var bookedRoomList: MutableList<RoomModel> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recyclerRoom)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadRoomList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.booked_room_list, container, false)
    }

    private fun loadRoomList(){
        val stringRequest =
            object : StringRequest(Method.POST, roomURL, Response.Listener { response:String ->
                val roomList: JSONArray = JSONArray(response)
                for (i: Int in 0 until roomList.length())
                {
                    val roomObject: JSONObject = roomList.getJSONObject(i)
                    val roomModel= RoomModel()
                    roomModel.id = Integer.parseInt(roomObject.getString("id"));
                    roomModel.size= roomObject.getString("size")
                    roomModel.style= roomObject.getString("style")
                    roomModel.note = roomObject.getString("note")
                    roomModel.status = roomObject.getString("status")
                    roomModel.price = roomObject.getString("price").toDouble().toFloat()
                    bookedRoomList.add(roomModel)
                }
                adapter = BookedRoomListAdapter(requireContext(), bookedRoomList)
                recyclerView.adapter = adapter
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }){
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["status"] = "booked"
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}