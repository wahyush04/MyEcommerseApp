package com.wahyush04.androidphincon

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wahyush04.androidphincon.databinding.ActivityMainBinding
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.androidphincon.ui.register.RegisterActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: PreferenceHelper
    private var idLocale: String = "en"
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

        sharedPreferences = PreferenceHelper(this)
        Log.d("tokenLogin", sharedPreferences.getPreference(Constant.TOKEN).toString())
        Log.d("tokenLogin", sharedPreferences.getPreference(Constant.ID).toString())
        setLocate()
        Log.d("localPref", sharedPreferences.getPreference(Constant.LOCALE).toString())

        setAppBar()

        val token: String = sharedPreferences.getToken(Constant.TOKEN).toString()
        Log.d("TOKEN", token)
        val rtoken: String = sharedPreferences.getToken(Constant.REFRESH_TOKEN).toString()
        Log.d("TOKEN", rtoken)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setAppBar(){
        val actionBar = supportActionBar
        actionBar?.setLogo(R.drawable.logo)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(true)
        val colorDrawable = ColorDrawable(Color.parseColor("#FFC7CD"))
        actionBar?.elevation = 0F
        actionBar!!.setBackgroundDrawable(colorDrawable)
    }

    private fun setLocate() {
        val localeID : String? = sharedPreferences.getPreference(Constant.LOCALE).toString()
        if (localeID == null){
            idLocale = "en"
        }else if (localeID == "2"){
            idLocale = "in"
        }

        val locale = Locale(idLocale)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, this.resources.displayMetrics)
    }
}