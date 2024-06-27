package com.example.appointmentapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val btn=findViewById<Button>(R.id.start)
        btn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}