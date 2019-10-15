package com.zeitform.wasserapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.zeitform.wasserapp.prefmanagers.PrefManager

import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long=2000 // 2 sec
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        prefManager = PrefManager(this)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            //startActivity(Intent(this,MainActivity::class.java))
            launchNextScreen()
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

    private fun launchNextScreen() {
        if (!prefManager!!.isFirstTimeLaunch) {
            startActivity(Intent(this,MainActivity::class.java))
            println("Main activity launched!")
        } else {
            startActivity(Intent(this,IntroActivity::class.java))
            println("Intro activity launched!")
        }
    }

}
