package com.wahyush04.androidphincon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.wahyush04.androidphincon.databinding.ActivityMainBinding
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.Constant

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = PreferenceHelper(this)

        val actionBar = supportActionBar
        actionBar?.setLogo(R.drawable.logo)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(true)
        val token: String = sharedPreferences.getToken(Constant.TOKEN).toString()
        Log.d("TOKEN", token)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to Logout")
                    .setPositiveButton("Ya") { _, _ ->
                        logout()
                        Toast.makeText(
                            applicationContext,
                            "Log out Berhasil",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .setNegativeButton("No") { _, _ ->
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout(){
        sharedPreferences.clear()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}