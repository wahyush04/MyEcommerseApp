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
import com.wahyush04.androidphincon.ui.successpage.SuccessPageActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.database.DataTrolley
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatterIdr
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartListAdapter
    private lateinit var favDao: ProductDao
    private lateinit var cartViewModel : CartViewModel
    private var totalPrice : Int? = 0
    private lateinit var preferences : PreferenceHelper
    private lateinit var mutableListItem : MutableList<DataStockItem>
    private lateinit var id : MutableList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        preferences = PreferenceHelper(this)
        mutableListItem = mutableListOf()
        id = mutableListOf<Int>()

        cartViewModel = ViewModelProvider(this)[CartViewModel::class.java]
        doActionClicked()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.checkboxSelectAll.isChecked = preferences.getIsCheck(Constant.ISCHECK)

        binding.checkboxSelectAll.setOnCheckedChangeListener { p0, p1 ->
            Toast.makeText(this@CartActivity, p1.toString(), Toast.LENGTH_SHORT).show()
            if (p1){
                preferences.putCheck(true)
                cartViewModel.checkAll(1)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            } else {
                preferences.putCheck(false)
                cartViewModel.checkAll(0)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            }
        }

        initData()

        binding.btnBuy.setOnClickListener {
            val result = cartViewModel.getTotalHarga()
            binding.tvTotalPrice.text = result.toString().formatterIdr()

            val checkedTrolley =  cartViewModel.getTrolleyChecked()
            for (item in checkedTrolley!!){
                mutableListItem.add(DataStockItem(item.id.toString(), item.stock_buy))
                id.add(item.id)
            }

            val listListRequestItem: List<DataStockItem> = mutableListItem.toList()
            val idList = id.toIntArray()
            val requestBody = UpdateStockRequestBody(listListRequestItem)
            Log.d("checkedTrolley", requestBody.toString())
            buyProduct(requestBody)
            cartViewModel.deleteTrolleyChecked()
            Toast.makeText(this, "Pembelian Berhasil", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SuccessPageActivity::class.java)
            intent.putExtra("data", idList)
            startActivity(Intent(this, SuccessPageActivity::class.java))
            startActivity(intent)
        }

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
            {
                val schema = "data_stock"
                val idProduct = it.id
                val buyProduct = it.stock
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
                }
            } else {
                binding.checkboxSelectAll.isChecked = false
                cartViewModel.checkAll(0)
            }
        }
    }

    private fun buyProduct(requestBody : UpdateStockRequestBody){
        cartViewModel.setBuyProduct(requestBody, preferences, this)
    }
}