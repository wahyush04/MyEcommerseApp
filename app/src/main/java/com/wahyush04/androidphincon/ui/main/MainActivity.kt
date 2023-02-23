package com.wahyush04.androidphincon.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wahyush04.androidphincon.BaseFirebaseAnalytics
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityMainBinding
import com.wahyush04.androidphincon.ui.cart.CartActivity
import com.wahyush04.androidphincon.ui.notification.NotificationActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: PreferenceHelper
    private var idLocale: String = "en"
    private val mainViewModel: MainViewModel by viewModels()
    private var totalTrolley : Int? = 0
    private var totalNotification : Int? =0
    private val firebaseAnalytics = BaseFirebaseAnalytics()
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = PreferenceHelper(this)
        setLocate()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setIconBadges()
        binding.icCart.setOnClickListener {
            //GA Slide 9 & 11 onCLickTrolleyIcon
            firebaseAnalytics.onClickButton("Home", "Trolley Icon")
            startActivity(Intent(this@MainActivity, CartActivity::class.java))
        }
        binding.icNotif.setOnClickListener {
            //GA Slide 9 & 11 onClickNotifIcon
            firebaseAnalytics.onClickButton("Home", "Notif Icon")
            startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
        }
    }

    private fun setLocate() {
        val localeID : String = sharedPreferences.getPreference(Constant.LOCALE).toString()
        if (localeID == null){
            idLocale = "en"
            sharedPreferences.putLocale("1")
        }else if (localeID == "2"){
            idLocale = "in"
        }

        val locale = Locale(idLocale)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, this.resources.displayMetrics)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                Toast.makeText(applicationContext, "Cart", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setIconBadges()
    }

    private fun setIconBadges(){
        totalTrolley = mainViewModel.countTrolley()
        totalNotification = mainViewModel.countNotif()
        binding.apply {
            if (totalTrolley == 0) {
                tvBadgesValue.visibility = View.INVISIBLE
                imgBadges.visibility = View.INVISIBLE
            } else {
                imgBadges.visibility = View.VISIBLE
                tvBadgesValue.visibility = View.VISIBLE
                tvBadgesValue.text = totalTrolley.toString()
            }

            if (totalNotification == 0){
                tvBadgesNotifValue.visibility = View.INVISIBLE
                imgBadgesNotif.visibility = View.INVISIBLE
            } else{
                imgBadgesNotif.visibility = View.VISIBLE
                tvBadgesNotifValue.visibility = View.VISIBLE
                tvBadgesNotifValue.text = totalNotification.toString()
            }
        }
    }

}