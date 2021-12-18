package com.example.hotelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import com.example.hotelapp.common.Common
import com.example.hotelapp.ui.personal.PersonalFragment
import com.example.hotelapp.ui.service.ServiceFragment
import com.example.hotelapp.ui.userinfo.UserInfoFragment
import com.example.yummy.ui.favorite.RoomFragment
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private var cardRoom: CardView?= null
    private var cardService: CardView?= null
    private var cardStatistic: CardView?= null
    private var cardUserInfo: CardView?= null
    private var cardLanguage: CardView?= null
    private var cardPersonal: CardView?= null
    private var tvUserName: TextView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        init()

        cardRoom!!.setOnClickListener {
            val roomFragment = RoomFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.main_layout, roomFragment).addToBackStack(null).commit();
        }
        cardService!!.setOnClickListener {
            val serviceFragment = ServiceFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.main_layout, serviceFragment).addToBackStack(null).commit();
        }
        cardStatistic!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardLanguage!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardUserInfo!!.setOnClickListener {
            val userInfoFragment = UserInfoFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.main_layout, userInfoFragment).addToBackStack(null).commit();
        }
        cardPersonal!!.setOnClickListener {
            if(Common.currentUser?.position == "Manager") {
                val personalFragment = PersonalFragment()
                val fragmentManager: FragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().add(R.id.main_layout, personalFragment)
                    .addToBackStack(null).commit();
            }else{
                Toast.makeText(this, "Sorry! You don't have permission to access this", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init() {
        cardRoom = findViewById(R.id.card_room)
        cardService = findViewById(R.id.card_service)
        cardStatistic = findViewById(R.id.card_statistic)
        cardLanguage = findViewById(R.id.card_language)
        cardUserInfo = findViewById(R.id.card_userinfo)
        cardPersonal = findViewById(R.id.card_personal)
        tvUserName = findViewById(R.id.txt_username)
        tvUserName!!.text = StringBuilder("Welcome, "+ Common.currentUser!!.username)
    }

}