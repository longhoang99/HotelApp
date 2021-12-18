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
import com.example.hotelapp.common.Common
import com.example.hotelapp.model.RoomModel
import com.example.hotelapp.model.ServiceModel
import com.example.hotelapp.model.UserModel

class UserListAdapter(private val context: Context, private val userList: MutableList<UserModel>): RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    private val addURL = "http://10.0.2.2:8080/hotel_app/addUser.php"
    private val editURL = "http://10.0.2.2:8080/hotel_app/editUser.php"
    private val deleteURL = "http://10.0.2.2:8080/hotel_app/deleteUser.php"
    class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userId = itemView.findViewById(R.id.txt_user_id) as TextView
        var userName = itemView.findViewById(R.id.txt_fullname) as TextView
        var userPosition = itemView.findViewById(R.id.txt_position) as TextView
        var userEdit: ImageView ?= itemView.findViewById(R.id.img_edit_user)
        var userDelete: ImageView ?= itemView.findViewById(R.id.img_delete_user)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        val user = userList[position]
        holder.userId.text = StringBuilder(position.plus(1).toString()+",")
        holder.userName.text = userList[position].fullname
        holder.userPosition.text = StringBuilder("Position: "+userList[position].position)
        holder.itemView.setOnClickListener {
            showDetailDialog(user)
        }
        holder.userEdit?.setOnClickListener {
            showEditDialog(user, position)
        }
        holder.userDelete?.setOnClickListener {
            showConfirmDialog(position)
        }
    }

    private fun showDetailDialog(userModel: UserModel) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.personal_info_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
        val infoDialog = builder.show()
        dialogView.findViewById<TextView>(R.id.txt_username).setText(StringBuilder("User Name: "+userModel.username))
        dialogView.findViewById<TextView>(R.id.txt_fullname).setText(StringBuilder("Full Name: "+userModel.fullname))
        dialogView.findViewById<TextView>(R.id.txt_birthday).setText(StringBuilder("Birthday: "+userModel.birthday))
        dialogView.findViewById<TextView>(R.id.txt_sex).setText(StringBuilder("Sex: "+userModel.sex))
        dialogView.findViewById<TextView>(R.id.txt_email).setText(StringBuilder("Email: "+userModel.email))
        dialogView.findViewById<TextView>(R.id.txt_phone).setText(StringBuilder("Phone: "+userModel.phone))
        dialogView.findViewById<TextView>(R.id.txt_position).setText(StringBuilder("Position: "+userModel.position))
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            infoDialog.dismiss()
        }
    }

    fun showAddDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.personal_add_user_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Add New User")
        val editDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        val userModel= UserModel()
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            val password = dialogView.findViewById<EditText>(R.id.edt_password).text.toString()
            if (password.length < 6 || password.length > 32)
                Toast.makeText(
                    context,
                    "Password must be >= 6 and <=32 characters",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                userModel.username =
                    dialogView.findViewById<EditText>(R.id.edt_username).text.toString()
                userModel.fullname =
                    dialogView.findViewById<EditText>(R.id.edt_fullname).text.toString()
                userModel.birthday =
                    dialogView.findViewById<EditText>(R.id.edt_birthday).text.toString()
                userModel.sex = dialogView.findViewById<EditText>(R.id.edt_sex).text.toString()
                userModel.email = dialogView.findViewById<EditText>(R.id.edt_email).text.toString()
                userModel.phone = dialogView.findViewById<EditText>(R.id.edt_phone).text.toString()
                userModel.password = password
                val radioButton =
                    dialogView.findViewById<RadioGroup>(R.id.rdi_group).checkedRadioButtonId
                userModel.position =
                    dialogView.findViewById<RadioButton>(radioButton).text.toString()
                AddUser(userModel)
                editDialog.dismiss()

            }
        }
    }

    private fun AddUser(userModel: UserModel) {
        val stringRequest =
            object : StringRequest(Method.POST, addURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    userList.add(userModel)
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
                    params["username"] = userModel.username.toString()
                    params["fullname"] = userModel.fullname.toString()
                    params["birthday"] = userModel.birthday.toString()
                    params["sex"] = userModel.sex.toString()
                    params["email"] = userModel.email.toString()
                    params["phone"] = userModel.phone.toString()
                    params["password"] = userModel.password.toString()
                    if(userModel.position.toString() == "Manager"){
                        params["position"] = "1"
                    }else
                        params["position"] = "0"
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }


    private fun showEditDialog(userModel: UserModel, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.personal_edit_info_dialog, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Edit Information")
        val username = dialogView.findViewById<EditText>(R.id.edt_username)
        username.setText(userModel.username)
        val fullname = dialogView.findViewById<EditText>(R.id.edt_fullname)
        fullname.setText(userModel.fullname)
        val birthday = dialogView.findViewById<EditText>(R.id.edt_birthday)
        birthday.setText(userModel.birthday)
        val sex = dialogView.findViewById<EditText>(R.id.edt_sex)
        sex.setText(userModel.sex)
        val email = dialogView.findViewById<EditText>(R.id.edt_email)
        email.setText(userModel.email)
        val phone = dialogView.findViewById<EditText>(R.id.edt_phone)
        phone.setText(userModel.phone)
        if(userModel.position == dialogView.findViewById<RadioButton>(R.id.rdi_manager).text){
            dialogView.findViewById<RadioButton>(R.id.rdi_manager).isChecked
        }
        val editDialog = builder.show()
        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            editDialog.dismiss()
        }
        dialogView.findViewById<Button>(R.id.btn_ok).setOnClickListener {
            userModel.username = username.text.toString()
            userModel.fullname = fullname.text.toString()
            userModel.birthday = birthday.text.toString()
            userModel.sex = sex.text.toString()
            userModel.email = email.text.toString()
            userModel.phone = phone.text.toString()
            val radioButton = dialogView.findViewById<RadioGroup>(R.id.rdi_group).checkedRadioButtonId
            userModel.position = dialogView.findViewById<RadioButton>(radioButton).text.toString()
            editUserInfo(userModel, position)
            editDialog.dismiss()
        }
    }

    private fun editUserInfo(userModel: UserModel, position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, editURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    userList[position] = userModel
                    Common.currentUser = userModel
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
                    params["id"] = userModel.userId.toString()
                    params["username"] = userModel.username.toString()
                    params["fullname"] = userModel.fullname.toString()
                    params["birthday"] = userModel.birthday.toString()
                    params["sex"] = userModel.sex.toString()
                    params["email"] = userModel.email.toString()
                    params["phone"] = userModel.phone.toString()
                    if(userModel.position.toString() == "Manager"){
                        params["position"] = "1"
                    }else
                        params["position"] = "0"
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
            deleteUser(position)
        }
        alertDialog.show()
    }

    private fun deleteUser(position: Int) {
        val stringRequest =
            object : StringRequest(Method.POST, deleteURL, Response.Listener{ response: String ->
                if (response.equals("Success")) {
                    userList.removeAt(position)
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
                    params["id"] = userList[position].userId.toString()
                    return params
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}