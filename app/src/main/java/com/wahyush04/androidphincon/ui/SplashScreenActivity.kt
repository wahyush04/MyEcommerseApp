package com.wahyush04.androidphincon.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.BaseFirebaseAnalytics
import com.wahyush04.core.helper.PreferenceHelper


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var img : ImageView
    private lateinit var sharedPreferences: PreferenceHelper
    private val firebaseAnalytics = BaseFirebaseAnalytics()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        supportActionBar?.hide()
        sharedPreferences = PreferenceHelper(this)

        img = findViewById(R.id.iv_logo)
        img.animate().setDuration(1500).translationY(-800f).withEndAction{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //GAS Slide 3 onLoadScreen
        firebaseAnalytics.onLoadScreen(
            "Splash Screen",
            this.javaClass.simpleName)
    }

}