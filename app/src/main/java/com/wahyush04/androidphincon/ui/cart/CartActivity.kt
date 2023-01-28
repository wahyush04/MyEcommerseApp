package com.wahyush04.androidphincon.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.ActivityCartBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatterIdr
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartListAdapter
    private lateinit var favDao: ProductDao
    private lateinit var cartViewModel : CartViewModel
    private var totalPrice : Int? = 0
    private lateinit var preferences : PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        preferences = PreferenceHelper(this)

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        doActionClicked()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.checkboxSelectAll.setOnCheckedChangeListener(object : OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                Toast.makeText(this@CartActivity, p1.toString(), Toast.LENGTH_SHORT ).show()
                adapter.isCheckedAll(p1)

            }

        })

        initData()
        val result = cartViewModel.getTotalHarga()
        binding.tvTotalPrice.text = result.toString().formatterIdr()
    }


    private fun doActionClicked() {
        adapter = CartListAdapter(
            {
                lifecycleScope.launch {
                    cartViewModel.deleteCart(it)
                    val result = cartViewModel.getTotalHarga()
                    binding.tvTotalPrice.text = result.toString().formatterIdr()
                }
            },
            {
                val id = it.id
                cartViewModel.updateCheck(id, 1)
                val result = cartViewModel.getTotalHarga()
                Log.d("sampemana", "activity ini mah")
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val id = it.id
                cartViewModel.updateCheck(id, 0)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                val newTotalHarga = it.total_harga + it.harga
                cartViewModel.updateQuantity((quantity + 1), id,newTotalHarga)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                val newTotalHarga = it.total_harga - it.harga
                cartViewModel.updateQuantity((quantity - 1), id, newTotalHarga)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
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
//                    adapter.onItemClick = {
//                    }
                }
            }
        }
    }

    private fun buyProduct(){
        val requestBody = UpdateStockRequestBody(listOf())
        cartViewModel.setBuyProduct(requestBody, preferences, this)
    }
}