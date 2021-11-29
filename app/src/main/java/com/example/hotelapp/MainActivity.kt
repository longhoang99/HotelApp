package com.example.hotelapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    private var cardRoom: CardView?= null
    private var cardService: CardView?= null
    private var cardStatistic: CardView?= null
    private var cardUserInfo: CardView?= null
    private var cardLanguage: CardView?= null
    private var cardPersonal: CardView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        cardRoom!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardService!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardStatistic!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardLanguage!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardUserInfo!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
        cardPersonal!!.setOnClickListener {
            Toast.makeText(this, "Coming Soon!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        cardRoom = findViewById(R.id.card_room)
        cardService = findViewById(R.id.card_service)
        cardStatistic = findViewById(R.id.card_statistic)
        cardLanguage = findViewById(R.id.card_language)
        cardUserInfo = findViewById(R.id.card_userinfo)
        cardPersonal = findViewById(R.id.card_personal)
    }
}