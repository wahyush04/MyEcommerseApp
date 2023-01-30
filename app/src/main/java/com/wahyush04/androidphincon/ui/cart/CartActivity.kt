package com.wahyush04.androidphincon.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.wahyush04.core.database.ProductEntity
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
        initData()


        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.checkboxSelectAll.isChecked = preferences.getIsCheck(Constant.ISCHECK)

        binding.checkboxSelectAll.setOnCheckedChangeListener { _, p1 ->
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


        binding.btnBuy.setOnClickListener {
            showLoading(true)
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
            showLoading(false)
        }

        val result = cartViewModel.getTotalHarga()
        binding.tvTotalPrice.text = result.toString().formatterIdr()
    }


    private fun doActionClicked() {
        adapter = CartListAdapter(
            { cartViewModel.deleteCart(it)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
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
            this
        )
    }

    private fun initData() {
        binding.rvCart.adapter = adapter
        binding.rvCart.layoutManager = LinearLayoutManager(this@CartActivity)
        binding.rvCart.setHasFixedSize(true)
        cartViewModel.getTrolley().observe(this@CartActivity) {
            if (it.isNotEmpty()) {
                showEmpty(false)
                adapter.setData(it)
            } else {
                showEmpty(true)
                binding.checkboxSelectAll.isChecked = false
                cartViewModel.checkAll(0)
            }
        }
    }

    private fun buyProduct(requestBody : UpdateStockRequestBody){
        cartViewModel.setBuyProduct(requestBody, preferences, this)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.loadingDialog.loadingLayout.visibility = View.VISIBLE
        }else{
            binding.loadingDialog.loadingLayout.visibility = View.GONE
        }
    }

    private fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
            binding.cvNavBottom.visibility = View.GONE
            binding.checkboxSelectAll.visibility = View.GONE
            binding.tvCheckAll.visibility = View.GONE
            binding.rvCart.visibility = View.GONE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
            binding.cvNavBottom.visibility = View.VISIBLE
            binding.checkboxSelectAll.visibility = View.VISIBLE
            binding.tvCheckAll.visibility = View.VISIBLE
            binding.rvCart.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

    }
}