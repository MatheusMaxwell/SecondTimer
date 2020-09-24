package com.example.secondtimer

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        playerEnd = MediaPlayer.create(this, R.raw.time_end)
        playerTime = MediaPlayer.create(this, R.raw.time)
        playerClick = MediaPlayer.create(this, R.raw.click)

        MobileAds.initialize(this) {}

        val mAdView : AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        text_clock.setOnClickListener {
            click()
            if(endTimer == 0){
                Toast.makeText(
                    this,
                    resources.getString(R.string.time_not_found),
                    Toast.LENGTH_LONG
                ).show()
            }
            else{
                initTimer()
            }


        }

        btn_reset.setOnClickListener {
            click()
            reset()
        }

        btn_config.setOnClickListener {
            dialogConfig()
        }

        btn_help.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
    }

    private fun dialogConfig(){
        val dialog = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_config, null)
        val edtTotal = view.findViewById<TextView>(R.id.edt_total_time)
        val edtSecondCount = view.findViewById<TextView>(R.id.edt_second_count)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)

        dialog.setView(view)

        btnConfirm.setOnClickListener {
            if(edtTotal.text.isEmpty() || edtSecondCount.text.isEmpty())
                Toast.makeText(this, resources.getString(R.string.no_values), Toast.LENGTH_LONG).show()
            else{
                endTimer = edtTotal.text.toString().toInt()
                timeLeftToCount = edtSecondCount.text.toString().toInt()
            }
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun initTimer(){
        if(text_clock.text.toString().equals(resources.getString(R.string.start)) || isPaused){
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
        text_clock.text = resources.getString(R.string.start)
    }
}