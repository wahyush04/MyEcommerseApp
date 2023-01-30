package com.wahyush04.androidphincon.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityMainBinding
import com.wahyush04.androidphincon.ui.cart.CartActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: PreferenceHelper
    private var idLocale: String = "en"
    private lateinit var mainViewModel: MainViewModel
    private var totalTrolley : Int? = 0

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

//        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[MainViewModel::class.java]

        supportActionBar?.hide()

        sharedPreferences = PreferenceHelper(this)
        setLocate()
//        setAppBar()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        totalTrolley = mainViewModel.countTrolley()
        binding.apply {
            if (totalTrolley == 0) {
                tvBadgesValue.visibility = View.INVISIBLE
                imgBadges.visibility = View.INVISIBLE
            } else {
                imgBadges.visibility = View.VISIBLE
                tvBadgesValue.visibility = View.VISIBLE
                tvBadgesValue.text = totalTrolley.toString()
            }
            icCart.setOnClickListener {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
        }
    }

    private fun setLocate() {
        val localeID : String? = sharedPreferences.getPreference(Constant.LOCALE).toString()
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
        totalTrolley = mainViewModel.countTrolley()
        binding.apply {
            if (totalTrolley == 0) {
                tvBadgesValue.visibility = View.INVISIBLE
                imgBadges.visibility = View.INVISIBLE
            } else {
                imgBadges.visibility = View.VISIBLE
                tvBadgesValue.visibility = View.VISIBLE
                tvBadgesValue.text = totalTrolley.toString()
            }
        }
    }

}