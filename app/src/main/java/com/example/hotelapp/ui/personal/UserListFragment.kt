package com.example.hotelapp.ui.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
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
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel
import com.example.hotelapp.model.UserModel
import com.example.hotelapp.ui.adapter.AvailableRoomListAdapter
import com.example.hotelapp.ui.adapter.ServiceListAdapter
import com.example.hotelapp.ui.adapter.UserListAdapter
import com.google.android.material.tabs.TabLayout
import org.json.JSONArray
import org.json.JSONObject

class UserListFragment : Fragment() {

    private val userURL = "http://10.0.2.2:8080/hotel_app/userList.php"
    private lateinit var recyclerView: RecyclerView
    private var adapter: UserListAdapter?= null
    private var userList: MutableList<UserModel> = ArrayList()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = requireView().findViewById(R.id.recyclerUser)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val userAdd: ImageView ?=  view.findViewById(R.id.img_add)
        loadUserList()
        userAdd?.setOnClickListener {
            adapter?.showAddDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_list, container, false)
    }

    private fun loadUserList(){
        val stringRequest =
            object : StringRequest(userURL, Response.Listener { response:String ->
                val userArr: JSONArray = JSONArray(response)
                for (i: Int in 0 until userArr.length())
                {
                    val userObject: JSONObject = userArr.getJSONObject(i)
                    val user= UserModel()
                    user.userId = Integer.parseInt(userObject.getString("id"));
                    user.username = userObject.getString("username")
                    user.fullname = userObject.getString("fullname")
                    user.birthday = userObject.getString("birthday")
                    user.sex = userObject.getString("sex")
                    user.email = userObject.getString("email")
                    user.phone = userObject.getString("phone")
                    user.position= userObject.getString("position")
                    userList.add(user)
                }
                adapter = UserListAdapter(requireContext(), userList)
                recyclerView.adapter = adapter
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }){}
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

}