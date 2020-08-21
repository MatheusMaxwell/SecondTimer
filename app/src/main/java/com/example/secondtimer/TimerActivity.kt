package com.example.secondtimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.podcopic.animationlib.library.AnimationType
import com.podcopic.animationlib.library.StartSmartAnimation
import kotlinx.android.synthetic.main.activity_timer.*


class TimerActivity : AppCompatActivity() {
    private var playerEnd = MediaPlayer()
    private var playerTime = MediaPlayer()
    private var playerClick = MediaPlayer()
    private var timer = 0
    private var endTimer = 0
    private var timeLeftToCount = 0
    private var isPaused = false
    private var handler: Handler = Handler()
    private var isReset = false
    //private var chronometer: Chronometer ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        //chronometer = Chronometer(this)
        playerEnd = MediaPlayer.create(this, R.raw.time_end)
        playerTime = MediaPlayer.create(this, R.raw.time)
        playerClick = MediaPlayer.create(this, R.raw.click)

        //chronometer!!.start()
        text_clock.setOnClickListener {
            click()
            if(endTimer == 0){
                Toast.makeText(this, "Nenhum tempo informado.", Toast.LENGTH_LONG).show()
            }
            else{
                initTimer()
            }


        }

        btn_reset.setOnClickListener {
            click()
            reset()
        }
    }

    private fun initTimer(){
        if(text_clock.text.toString().equals("Start") || isPaused){
            isPaused = false
            isReset = false
            handler.postDelayed({
                start()
            }, 1000)
        }
        else{
            isPaused = true
        }
    }

    private fun start(){
        if(!isPaused && !isReset){
            timer++
            if((timer >= endTimer - timeLeftToCount) && timer != endTimer)
                startAnimationSecond()

            if(timer == endTimer)
                startAnimationEnd()
            else {
                handler.postDelayed({
                    start()
                }, 1000)
            }

            changeTextTimer()
        }

    }

    private fun changeTextTimer(){
        if(timer < 10){
            text_clock.text = "0$timer"
        }
        else{
            text_clock.text = timer.toString()
        }
    }

    private fun startAnimationEnd(){
        playerEnd.start()
        StartSmartAnimation.startAnimation(frame_timer, AnimationType.Swing, 2000, 0, false)

    }

    private fun startAnimationSecond(){
        playerTime.start()
        StartSmartAnimation.startAnimation(frame_timer, AnimationType.Flash, 500, 0, true)
    }

    private fun click(){
        playerClick.start()
    }

    private fun reset(){
        isReset = true
        isPaused = false
        timer = 0
        isPaused = false
        text_clock.text = "Start"
    }
}