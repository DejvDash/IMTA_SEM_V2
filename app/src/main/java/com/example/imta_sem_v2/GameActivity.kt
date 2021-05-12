package com.example.imta_sem_v2

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.File
import java.time.LocalDateTime
import kotlin.concurrent.fixedRateTimer


class GameActivity : AppCompatActivity() {
    class CustomTouchListener(

        val screenWidth: Int,
        val screenHeight: Int

    ) : View.OnTouchListener {
        private var dX: Float = 0f
        private var dY: Float = 0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {

            val newX: Float
            val newY: Float

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX

                }
                MotionEvent.ACTION_MOVE -> {

                    newX = event.rawX + dX


                    if ((newX <= 0 || newX >= screenWidth - view.width)) {
                        return true
                    }

                    view.animate()
                        .x(newX)

                        .setDuration(0)
                        .start()
                }
            }
            return true
        }
    }



    //Definice globalnich promennych
    var scoreCount: Int = 0
    var lifeCount: Int = 3
    var timerPeriod = 1000
    var animPeriod = 2000
    public lateinit var pathToGameActivity:String


    var fixedRateTimerRock = fixedRateTimer(
        name = "rock-spawner",
        initialDelay = 1000, period = 2000
    ) {
    }
    var fixedRateTimerMissile = fixedRateTimer(
        name = "rock-spawner",
        initialDelay = 1000, period = 2000
    ) {
    }
    private lateinit var ship: ImageView
    private lateinit var score: TextView
    private lateinit var lives: TextView
    private lateinit var gameScreen: ConstraintLayout

    var rockList: MutableList<ImageView> = mutableListOf<ImageView>()


    fun spawnMissile(){
        var missileIMV: ImageView = ImageView(this)
        gameScreen.addView(missileIMV)
        missileIMV.layoutParams.height = ship.height/5
        missileIMV.layoutParams.width = ship.width/5

        var shipBounds = Rect()
        ship.getHitRect(shipBounds)

        var xFloatMissile = (shipBounds.centerX()).toFloat() - shipBounds.width()/12
        var yFloatMissile = (shipBounds.top-ship.height/4).toFloat()

        missileIMV.x = xFloatMissile
        missileIMV.y = yFloatMissile
        missileIMV.setImageResource(R.drawable.missile)
        var missileFlight = ObjectAnimator.ofFloat(missileIMV, "translationY",
                (shipBounds.centerY() - gameScreen.height).toFloat()
        ).apply {
            duration = animPeriod.toLong()
            start()
        }

        missileFlight.addUpdateListener(AnimatorUpdateListener {
            var rocketBounds = Rect()
            missileIMV.getHitRect(rocketBounds)
            var rockIterator = rockList.iterator()
            if (rocketBounds.top + rocketBounds.height() < gameScreen.top) {

                gameScreen.removeView(missileIMV)


            } else {
                while (rockIterator.hasNext()) {
                    var tmpBounds = Rect()
                    var next = rockIterator.next()
                    next.getHitRect(tmpBounds)
                    if (rocketBounds.intersect(tmpBounds)) {

                        gameScreen.removeView(missileIMV)
                        gameScreen.removeView(next)
                        scoreCount++
                        score.setText("Score: $scoreCount")

                        rockList.remove(next)
                        break;
                    }


                }
            }

        })



    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun spawnRock(){

        var rockIMV: ImageView = ImageView(this)


        gameScreen.addView(rockIMV)
        rockList.add(rockIMV)

        rockIMV.layoutParams.height = gameScreen.width/6
        rockIMV.layoutParams.width = gameScreen.width/6


        var xFloatRock = (Math.random()*gameScreen.right).toFloat()
        var yFloatRock = -200F


        rockIMV.x = xFloatRock
        rockIMV.y = yFloatRock
        rockIMV.setImageResource(R.drawable.meteorite)


        var xFloatRockToMove = 0F
        var yFloatRockToMove = 0F

        if (xFloatRock > gameScreen.right/2){
            xFloatRockToMove = (Math.random()*gameScreen.right/2).toFloat()
        }else{
            xFloatRockToMove = (Math.random()*gameScreen.right/2+gameScreen.right/2).toFloat()
        }

        ObjectAnimator.ofFloat(rockIMV, "translationX", xFloatRockToMove).apply {
            duration = (animPeriod-scoreCount).toLong()
            start()
        }
        var decidingAnim =  ObjectAnimator.ofFloat(
            rockIMV,
            "translationY",
                gameScreen.bottom.toFloat() - 50F
        ).apply {
            duration = (animPeriod-scoreCount).toLong()
            start()


        }

        decidingAnim.addUpdateListener(AnimatorUpdateListener {
            var objectBounds = Rect()
            rockIMV.getHitRect(objectBounds)
            if (objectBounds.top > gameScreen.top) {

                if (gameScreen.bottom < objectBounds.bottom + 20) {

                    gameScreen.removeView(rockIMV)
                    rockList.remove(rockIMV)


                }
            }
            var shipBounds = Rect()
            ship.getHitRect(shipBounds)

            if (objectBounds.intersect(shipBounds)) {
                gameScreen.removeView(rockIMV)
                rockList.remove(rockIMV)
                lifeCount--
                lives.setText("Lives: $lifeCount")
                if (lifeCount == 0) {

                    pathToGameActivity = cacheDir.absolutePath
                    val file = File("$pathToGameActivity/score.txt")
                    file.appendText("Game: ")
                    file.appendText((LocalDateTime.now()).toString()+" ")
                    file.appendText("End score: $scoreCount \n")

                    fixedRateTimerMissile.cancel()
                    fixedRateTimerRock.cancel()
                    showHide(ship)

                    val intent = Intent(this@GameActivity, PopUpWindow::class.java)
                    intent.putExtra("popuptext", "Score : $scoreCount")
                    startActivity(intent)
                    finish()

                }
            }
        })

    }

    fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        //var shipBounds = Rect()
        //  ship.getHitRect(shipBounds)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameScreen =  findViewById<ConstraintLayout>(R.id.gameScreenView)
        gameScreen.viewTreeObserver.addOnGlobalLayoutListener { ship.setOnTouchListener(
            CustomTouchListener(
                gameScreen.width,
                gameScreen.height
            )
        ) }

        ship = findViewById<ImageView>(R.id.shipView)
        score = findViewById<TextView>(R.id.score)
        lives = findViewById<TextView>(R.id.life)

         fixedRateTimerRock = fixedRateTimer(
             name = "rock-spawner",
             initialDelay = 1000, period = timerPeriod.toLong()-scoreCount/2
         ) {

            runOnUiThread {
                spawnRock()

            }
        }

         fixedRateTimerMissile = fixedRateTimer(
             name = "missile-spawner",
             initialDelay = 1000, period =timerPeriod.toLong()-100-scoreCount/10
         ) {

            runOnUiThread {
                spawnMissile()

            }
        }



    }
}