package com.example.hotelapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
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
import com.example.hotelapp.model.UserModel
import dmax.dialog.SpotsDialog
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var edtEmail: EditText?= null
    private var edtFullName : EditText?= null
    private var edtUserName : EditText?= null
    private var edtPhone: EditText?= null
    private var edtPassword: EditText?= null
    private var btnRegister: Button?= null
    private var tvLogin : TextView?= null
    private val URL = "http://10.0.2.2:8080/hotel_app/signup.php"
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        dialog = SpotsDialog.Builder().setContext(this).setCancelable(false).build()
        edtEmail = findViewById(R.id.edt_email)
        edtFullName = findViewById(R.id.edt_fullname)
        edtUserName = findViewById(R.id.edt_username)
        edtPhone = findViewById(R.id.edt_phone)
        edtPassword = findViewById(R.id.edt_password)
        btnRegister = findViewById(R.id.btn_register)
        tvLogin = findViewById(R.id.tv_login)
        btnRegister!!.setOnClickListener {
            val userModel = UserModel()
            userModel.username = edtUserName!!.text.toString().trim()
            userModel.email = edtEmail!!.text.toString().trim()
            userModel.fullname = edtFullName!!.text.toString().trim()
            userModel.password = edtPassword!!.text.toString().trim()
            userModel.phone = edtPhone!!.text.toString().trim()
            userModel.isAdmin = 1
            if (TextUtils.isEmpty(userModel.username) || TextUtils.isEmpty(userModel.email) || TextUtils.isEmpty(userModel.fullname) || TextUtils.isEmpty(
                    userModel.password
                ) || TextUtils.isEmpty(userModel.phone)
            )
                Toast.makeText(this, "You have fill all information!", Toast.LENGTH_SHORT).show()
            else if (userModel.password!!.length < 6 || userModel.password!!.length > 32)
                Toast.makeText(this, "Password must be >= 6 and <=32 characters", Toast.LENGTH_SHORT).show()
            else {
                val stringRequest =
                    object : StringRequest(Method.POST, URL, Response.Listener{ response: String ->
                        if (response.equals("Sign Up Success")) {
                            Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Register Failed: username/email/phone already exist!", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener{ error: VolleyError? ->
                        Toast.makeText(this, "" + error!!.message, Toast.LENGTH_SHORT).show()
                    }) {
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["username"] = userModel.username!!
                            params["fullname"] = userModel.fullname!!
                            params["email"] = userModel.email!!
                            params["phone"] = userModel.phone!!
                            params["password"] = userModel.password!!
                            params["isadmin"] = userModel.isAdmin.toString()
                            return params
                        }
                    }
                val requestQueue: RequestQueue = Volley.newRequestQueue(this)
                requestQueue.add(stringRequest)
            }
        }
        tvLogin!!.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
            finish()

        }
    }
}