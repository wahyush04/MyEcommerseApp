package com.wahyush04.androidphincon.ui.cart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityCartBinding
import com.wahyush04.androidphincon.ui.changepassword.ChangePasswordViewModel
import com.wahyush04.core.database.ProductDao

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartListAdapter
    private lateinit var favDao: ProductDao
    private lateinit var cartViewModel : CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        adapter = CartListAdapter {cartViewModel.deleteCart(it)}

        initData()
        binding.btnBack.setOnClickListener {
            this@CartActivity.onBackPressed()
        }
    }

    fun addQuantity() {
        TODO("Not yet implemented")
    }

    fun decQuantity() {
        TODO("Not yet implemented")
    }

    private fun initData() {
        cartViewModel.getTrolley().observe(this@CartActivity) {
            if (it.isNotEmpty()) {
                adapter.setData(it)
                binding.apply {
                    rvCart.adapter = adapter
                    rvCart.layoutManager = LinearLayoutManager(this@CartActivity)
                    rvCart.setHasFixedSize(true)
                    adapter.onItemClick = {
                    }
                }
            }
        }
    }
}