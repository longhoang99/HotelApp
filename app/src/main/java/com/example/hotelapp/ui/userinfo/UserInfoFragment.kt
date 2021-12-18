package com.example.hotelapp.ui.userinfo

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.hotelapp.R
import com.example.hotelapp.common.Common
import com.example.hotelapp.databinding.FragmentUserInfoBinding
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.UserModel
import kotlinx.coroutines.test.withTestContext
import java.lang.StringBuilder

class UserInfoFragment : Fragment() {
    private var _binding:FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private val editURL = "http://10.0.2.2:8080/hotel_app/editInfo.php"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentUserInfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        binding.btnEdtInfo.setOnClickListener {
            showEditDialog(Common.currentUser)
        }
    }

    private fun setUpView() {
        binding.txtUsername.text = StringBuilder("User: "+Common.currentUser?.username)
        binding.txtFullname.text = StringBuilder("Full Name: "+Common.currentUser?.fullname)
        binding.txtBirthday.text = StringBuilder("Birthday: "+Common.currentUser?.birthday)
        binding.txtSex.text = StringBuilder("Sex: "+Common.currentUser?.sex)
        binding.txtEmail.text = StringBuilder("Email: "+Common.currentUser?.email)
        binding.txtPhone.text = StringBuilder("Phone: "+Common.currentUser?.phone)
        binding.txtPosition.text = StringBuilder("Position: "+Common.currentUser?.position)
    }

    private fun showEditDialog(userModel: UserModel?){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.user_edit_info_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit Information")
        val username = dialogView.findViewById<EditText>(R.id.edt_username)
        username.setText(userModel?.username)
        val fullname = dialogView.findViewById<EditText>(R.id.edt_fullname)
        fullname.setText(userModel?.fullname)
        val birthday = dialogView.findViewById<EditText>(R.id.edt_birthday)
        birthday.setText(userModel?.birthday)
        val sex = dialogView.findViewById<EditText>(R.id.edt_sex)
        sex.setText(userModel?.sex)
        val email = dialogView.findViewById<EditText>(R.id.edt_email)
        email.setText(userModel?.email)
        val phone = dialogView.findViewById<EditText>(R.id.edt_phone)
        phone.setText(userModel?.phone)
        val editDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            userModel?.username = username.text.toString()
            userModel?.fullname = fullname.text.toString()
            userModel?.birthday = birthday.text.toString()
            userModel?.sex = sex.text.toString()
            userModel?.email = email.text.toString()
            userModel?.phone = phone.text.toString()
            editInfo(userModel)
            editDialog.dismiss()
        }
    }

    private fun editInfo(userModel: UserModel?) {
        val stringRequest =
            object : StringRequest(Method.POST, editURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    Common.currentUser = userModel
                    setUpView()
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error! $response", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, "" + error!!.message, Toast.LENGTH_SHORT).show()
            }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["id"] = userModel?.userId.toString()
                    params["username"] = userModel?.username.toString()
                    params["fullname"] = userModel?.fullname.toString()
                    params["birthday"] = userModel?.birthday.toString()
                    params["sex"] = userModel?.sex.toString()
                    params["email"] = userModel?.email.toString()
                    params["phone"] = userModel?.phone.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}