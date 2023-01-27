package com.wahyush04.androidphincon.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.ActivityCartBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatterIdr

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartListAdapter
    private lateinit var favDao: ProductDao
    private lateinit var cartViewModel : CartViewModel
    private var totalPrice : Int = 0
    private lateinit var preferences : PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        preferences = PreferenceHelper(this)

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        doActionClicked()

        initData()
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.checkboxSelectAll.setOnCheckedChangeListener(object : OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                Toast.makeText(this@CartActivity, p1.toString(), Toast.LENGTH_SHORT ).show()
                adapter.isCheckedAll(p1)

            }

        })
    }


    private fun doActionClicked() {
        adapter = CartListAdapter(
            { cartViewModel.deleteCart(it) },
            {
                val result = adapter.totalValue
                cartViewModel.checkBox(it.id, true)
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val result = adapter.totalValue
                cartViewModel.checkBox(it.id, false)
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                cartViewModel.updateQuantity((quantity + 1), id)
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                cartViewModel.updateQuantity((quantity - 1), id)
            },
            {val schema = "data_stock"
                val idProduct = it.id
                val buyProduct = it.stock
                binding.btnBuy.setOnClickListener {
                    buyProduct()
                }
            }, this
        )
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

    private fun buyProduct(){
        val requestBody = UpdateStockRequestBody(listOf())
        cartViewModel.setBuyProduct(requestBody, preferences, this)
    }
}