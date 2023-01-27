package com.wahyush04.androidphincon

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.androidphincon.ui.register.RegisterActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var img : ImageView
    private lateinit var sharedPreferences: PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        sharedPreferences = PreferenceHelper(this)

        img = findViewById(R.id.iv_logo)
        img.animate().setDuration(2000).translationY(-1000f).withEndAction{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}