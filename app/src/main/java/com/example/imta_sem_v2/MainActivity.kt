package com.example.imta_sem_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button: Button = findViewById(R.id.playButton)
        button.setOnClickListener{
            val i = Intent(this@MainActivity,GameActivity::class.java)
            startActivity(i)
        }
    }
}