package com.example.hotelapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.UserModel
import dmax.dialog.SpotsDialog
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private var edtUserName: EditText? = null
    private var edtPassword: EditText? = null
    private var btnLogin: Button? = null
    private var tvSignup: TextView? = null
    private val URL = "http://10.0.2.2:8080/hotel_app/login.php"
    private val getUserURL = "http://10.0.2.2:8080/hotel_app/userInfo.php"
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        edtUserName = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        tvSignup = findViewById(R.id.tv_signup)
        if (Common.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        btnLogin!!.setOnClickListener {
            val userModel = UserModel()
            userModel.username = edtUserName!!.text.toString().trim()
            userModel.password = edtPassword!!.text.toString().trim()
            if (TextUtils.isEmpty(userModel.username) || TextUtils.isEmpty(userModel.password))
                Toast.makeText(this, "You have fill all information!", Toast.LENGTH_SHORT).show()
            else if (userModel.password!!.length < 6 || userModel.password!!.length > 32)
                Toast.makeText(
                    this,
                    "Password must be >= 6 and <=32 characters",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                val stringRequest =
                    object : StringRequest(Method.POST, URL, Response.Listener{ response: String ->
                        if (response.equals("Login Success")) {
                            Common.currentUser = getUserModel(userModel)
                            if (Common.currentUser!= null) {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }else Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Login Failed:\n$response", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener { error: VolleyError? ->
                        Toast.makeText(this, "" + error!!.message, Toast.LENGTH_SHORT).show()
                    }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["username"] = userModel.username!!
                            params["password"] = userModel.password!!
                            return params
                        }
                    }
                val requestQueue: RequestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }
        }
        tvSignup!!.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }

    private fun getUserModel(userModel: UserModel): UserModel {
        val stringRequest =
            object : StringRequest(Method.POST, getUserURL, Response.Listener{ response: String ->
                val userInfo: JSONArray = JSONArray(response)
                val user: JSONObject = userInfo.getJSONObject(0)
                userModel.userId = Integer.parseInt(user.getString("userId"))
                userModel.username = user.getString("username")
                userModel.fullname = user.getString("fullname")
                userModel.birthday = user.getString("birthday")
                userModel.sex = user.getString("sex")
                userModel.email = user.getString("email")
                userModel.phone = user.getString("phone")
                userModel.position = user.getString("position")
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(this, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["username"] = userModel.username!!
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
        return userModel
    }
}
