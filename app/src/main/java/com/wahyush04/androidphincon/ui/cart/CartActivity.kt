package com.wahyush04.androidphincon.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}