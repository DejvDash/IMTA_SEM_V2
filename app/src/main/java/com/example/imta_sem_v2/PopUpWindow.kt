package com.example.imta_sem_v2

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import org.w3c.dom.Text

class PopUpWindow : AppCompatActivity() {
    private var popupTitle = ""
    private var popupText = ""
    private var popupButton = ""
    private var darkStatusBar = false


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0,0)
        setContentView(R.layout.activity_pop_up_window)

        var s:String = intent.getStringExtra("popuptext").toString()
        val tvScore: TextView = findViewById(R.id.popup_window_text)
        tvScore.setText(s)


        val buttonPlay: Button = findViewById(R.id.popup_window_button)
        buttonPlay.setOnClickListener{
            onBackPressed()
        }

    }
    override fun onBackPressed() {
        val i = Intent(this@PopUpWindow, MainActivity::class.java)
        finish()
        //startActivity(i)
    }
}
