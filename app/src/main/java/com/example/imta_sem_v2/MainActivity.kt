package com.example.imta_sem_v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// THIS IS COMMIt
        //sssssAAAA

        val buttonPlay: Button = findViewById(R.id.playButton)
        buttonPlay.setOnClickListener{
            val i = Intent(this@MainActivity,GameActivity::class.java)
            startActivity(i)
        }

        val buttonExit: Button = findViewById(R.id.exitButton)
        buttonExit.setOnClickListener{
            finish()
        }

        val buttonScore: Button = findViewById(R.id.scoreboardButton)
        buttonScore.setOnClickListener{
            val i = Intent(this@MainActivity,ScoreboardActivity::class.java)
            startActivity(i)
        }
    }
}