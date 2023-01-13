package com.wahyush04.androidphincon.ui.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityChangePasswordBinding

class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}