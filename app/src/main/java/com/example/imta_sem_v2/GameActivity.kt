package com.example.imta_sem_v2

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.io.File
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
                    // dY = view.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {

                    newX = event.rawX + dX
                    //newY = event.rawY + dY

                    if ((newX <= 0 || newX >= screenWidth - view.width)) {
                        return true
                    }

                    view.animate()
                        .x(newX)
                        //.y(newY)
                        .setDuration(0)
                        .start()
                }
            }
            return true
        }
    }
    //  private val parentView by lazy{
    //     findViewById<View>(R.id.parentView)
    //  }



    var scoreCount: Int = 0
    var lifeCount: Int = 3
    var shot: Boolean = false;
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
    private lateinit var gameScreen: RelativeLayout

    var rockList: MutableList<ImageView> = mutableListOf<ImageView>()

   // private val gameScreen by lazy{
    //    findViewById<RelativeLayout>(R.id.gameScreenView)
  //  }

    fun spawnMissile(){
        var missileIMV: ImageView = ImageView(this)
        gameScreen.addView(missileIMV)
        missileIMV.layoutParams.height = 100
        missileIMV.layoutParams.width = 20

        var shipBounds = Rect()
        ship.getHitRect(shipBounds)

        var missileBounds = Rect()
        missileIMV.getHitRect(missileBounds)


        //println(shipBounds.centerX())
       // println(shipBounds.centerY())
        var xFloatMissile = (shipBounds.centerX() - 10).toFloat()
        var yFloatMissile = (shipBounds.top-ship.height/4).toFloat()
       // println(missileBounds.centerX())
        missileIMV.x = xFloatMissile
        missileIMV.y = yFloatMissile
        missileIMV.setImageResource(R.drawable.missile)
        var missileFlight = ObjectAnimator.ofFloat(
            missileIMV,
            "translationY",
            (shipBounds.centerY() - windowManager.currentWindowMetrics.bounds.height()).toFloat()
        ).apply {
            duration = 1000
            start()
        }

        missileFlight.addUpdateListener(AnimatorUpdateListener {
            var rocketBounds = Rect()
            missileIMV.getHitRect(rocketBounds)
            var rockIterator = rockList.iterator()
            if (rocketBounds.top + rocketBounds.height() < windowManager.currentWindowMetrics.bounds.top) {
                //println(objectBounds.top)
                //println(windowManager.currentWindowMetrics.bounds.top)
                // println(objectBounds.left)
                // println(objectBounds.right)


                //  println("-------------------------------------")
                //  println(int)
                //    println("true")
                gameScreen.removeView(missileIMV)
                //  println("missile is gone")

            } else {
                while (rockIterator.hasNext()) {
                    var tmpBounds = Rect()
                    var next = rockIterator.next()
                    next.getHitRect(tmpBounds)
                    if (rocketBounds.intersect(tmpBounds)) {
                        // println("it did this")
                        gameScreen.removeView(missileIMV)
                        gameScreen.removeView(next)
                        scoreCount++
                        score.setText("Score: $scoreCount")
                        // println(tmpBounds)
                        // println(rocketBounds)
                        rockList.remove(next)
                        break;
                    }


                }
            }

        })



    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun spawnRock(){

        var rockIMV: ImageView = ImageView(this)


        gameScreen.addView(rockIMV)
        rockList.add(rockIMV)

        rockIMV.layoutParams.height = 200
        rockIMV.layoutParams.width = 200


        var xFloatRock = (Math.random()*windowManager.currentWindowMetrics.bounds.right).toFloat()
        var yFloatRock = -200F


        rockIMV.x = xFloatRock
        rockIMV.y = yFloatRock
        rockIMV.setImageResource(R.drawable.meteorite)


        var xFloatRockToMove = 0F
        var yFloatRockToMove = 0F

        if (xFloatRock > windowManager.currentWindowMetrics.bounds.right/2){
            xFloatRockToMove = (Math.random()*windowManager.currentWindowMetrics.bounds.right/2).toFloat()
        }else{
            xFloatRockToMove = (Math.random()*windowManager.currentWindowMetrics.bounds.right/2+windowManager.currentWindowMetrics.bounds.right/2).toFloat()
        }

        ObjectAnimator.ofFloat(rockIMV, "translationX", xFloatRockToMove).apply {
            duration = 2000
            start()
        }
        var decidingAnim =  ObjectAnimator.ofFloat(
            rockIMV,
            "translationY",
            windowManager.currentWindowMetrics.bounds.bottom.toFloat() - 50F
        ).apply {
            duration = 2000
            start()


        }

        decidingAnim.addUpdateListener(AnimatorUpdateListener {
            var objectBounds = Rect()
            rockIMV.getHitRect(objectBounds)
            if (objectBounds.top > windowManager.currentWindowMetrics.bounds.top) {
                //println(objectBounds.top)
                //println(windowManager.currentWindowMetrics.bounds.top)
                // println(objectBounds.left)
                // println(objectBounds.right)
                if (windowManager.currentWindowMetrics.bounds.bottom < objectBounds.bottom + 20) {

                    //  println("-------------------------------------")
                    //  println(int)
                    // println("true")
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
                    // decidingAnim.cancel()
                    fixedRateTimerMissile.cancel()
                    fixedRateTimerRock.cancel()
                    //println( ship.visibility)

                    showHide(ship)

                    //Ulozeni dat do slozky
                    val directory: File
                    directory = if (filename.isEmpty()) {
                        filesDir
                    } else {
                        getDir(filename, MODE_PRIVATE)
                    }
                    val files = directory.listFiles()

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
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        //var shipBounds = Rect()
        //  ship.getHitRect(shipBounds)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameScreen =  findViewById<RelativeLayout>(R.id.gameScreenView)
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
             initialDelay = 200, period = 500
         ) {

            runOnUiThread {
                spawnRock()

            }
        }

         fixedRateTimerMissile = fixedRateTimer(
             name = "missile-spawner",
             initialDelay = 1000, period = 900
         ) {

            runOnUiThread {
                spawnMissile()

            }
        }


    }
}