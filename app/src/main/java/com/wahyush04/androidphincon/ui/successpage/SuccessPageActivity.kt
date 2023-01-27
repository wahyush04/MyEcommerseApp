package com.wahyush04.androidphincon.ui.successpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.databinding.ActivitySuccessPageBinding
import com.wahyush04.core.helper.PreferenceHelper

class SuccessPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySuccessPageBinding
    private lateinit var successPageViewModel : SuccessPageViewModel
    private lateinit var preferences: PreferenceHelper
    private var id : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        successPageViewModel = ViewModelProvider(this)[SuccessPageViewModel::class.java]
        preferences = PreferenceHelper(this)
        id = intent.getStringExtra("data")!!.toInt()

        binding.btnSubmitRating.setOnClickListener {
            updateStock()
        }
    }

    fun updateStock(){
        val rating =  binding.ratingBar.rating
        successPageViewModel.setUpdateResponse(preferences, this, id , rating.toString())
        successPageViewModel.getUpdateResponse().observe(this){data ->
            if (data.success.status == 201){
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}