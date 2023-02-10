package com.wahyush04.androidphincon.ui.successpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.databinding.ActivitySuccessPageBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.helper.PreferenceHelper

class SuccessPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySuccessPageBinding
    private lateinit var successPageViewModel : SuccessPageViewModel
    private lateinit var preferences: PreferenceHelper
    private var idList : List<Int>? = null
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        successPageViewModel = ViewModelProvider(this)[SuccessPageViewModel::class.java]
        preferences = PreferenceHelper(this)

        val idListIntArray = intent.extras?.getIntArray("data")
        idList = idListIntArray?.toList()
        Log.d("list", idList.toString())
        if (idList == null){
            id = intent.getStringExtra("data")!!.toInt()
            Log.d("idIntent", id.toString())
        }

        binding.btnSubmitRating.setOnClickListener {
            val rating =  binding.ratingBar.rating
            Log.d("idSuccess", id.toString())
            if (idList !== null){
                Log.d("idSuccess", id.toString())
                for (item in idList!!){
                    Log.d("idSuccessItem", id.toString())
                    successPageViewModel.setUpdateResponse(preferences, this, item , rating.toString())
                }
                successPageViewModel.getUpdateResponse().observe(this){data ->
                    if (data.success.status == 201){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            } else {
                updateStock()
            }

        }
    }

    private fun updateStock(){
        val rating =  binding.ratingBar.rating
        successPageViewModel.setUpdateResponse(preferences, this, id, rating.toString())
        successPageViewModel.getUpdateResponse().observe(this){data ->
            if (data.success.status == 201){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}