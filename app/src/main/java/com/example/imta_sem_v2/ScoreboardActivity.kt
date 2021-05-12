package com.example.imta_sem_v2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class ScoreboardActivity : AppCompatActivity() {
    lateinit var LW: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        LW = findViewById(R.id.LWScores)
        val path = cacheDir.absolutePath
        var listItems = mutableListOf<String>()

        val file = File("$path/score.txt")
        file.readLines().forEach(){listItems.add(it)}
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems)
        LW.adapter = adapter

        val buttonBack: Button = findViewById(R.id.backButton)
        buttonBack.setOnClickListener{
            finish()
        }



    }
}