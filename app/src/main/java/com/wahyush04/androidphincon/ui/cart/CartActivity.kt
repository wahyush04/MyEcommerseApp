package com.wahyush04.androidphincon.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.ActivityCartBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.ui.successpage.SuccessPageActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatterIdr

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
        var checkAll = preferences.getIsCheck(Constant.ISCHECK)

        binding.checkboxSelectAll.setOnClickListener {
            checkAll = !checkAll
            if (checkAll){
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
            binding.checkboxSelectAll.isChecked = checkAll
        }


        binding.btnBuy.setOnClickListener {
            val idUser =  preferences.getPreference(Constant.ID)
            val result = cartViewModel.getTotalHarga()
            binding.tvTotalPrice.text = result.toString().formatterIdr()

            val checkedTrolley =  cartViewModel.getTrolleyChecked()
            for (item in checkedTrolley!!){
                mutableListItem.add(DataStockItem(item.id.toString(), item.stock_buy))
                id.add(item.id)
            }

            val listListRequestItem: List<DataStockItem> = mutableListItem.toList()

            val requestBody = UpdateStockRequestBody(idUser!!,listListRequestItem)
            buyProduct(requestBody)

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
                cekCheckBox()
            },
            {
                val id = it.id
                cartViewModel.updateCheck(id, 0)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
                cekCheckBox()
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                val maxquantity = it.stock
                val newTotalHarga = it.total_harga + it.harga
                if (quantity == maxquantity){
                    Toast.makeText(this@CartActivity, "Out of Stock", Toast.LENGTH_SHORT).show()
                }else{
                    cartViewModel.updateQuantity((quantity + 1), id,newTotalHarga)
                }

                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            {
                val id = it.id
                val quantity = it.stockbuy
                val newTotalHarga = it.total_harga - it.harga
                if (quantity == 1){
                    Toast.makeText(this@CartActivity, "Quantity  cannot reduce", Toast.LENGTH_SHORT).show()
                }else{
                    cartViewModel.updateQuantity((quantity - 1), id, newTotalHarga)
                }
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
            },
            this
        )
        initData()
    }

    private fun initData() {
        binding.rvCart.adapter = adapter
        binding.rvCart.layoutManager = LinearLayoutManager(this@CartActivity)
        binding.rvCart.setHasFixedSize(true)
        cartViewModel.getTrolley()?.observe(this@CartActivity) {
            if (it.isNotEmpty()) {
                showEmpty(false)
                adapter.setData(it)
            } else {
                binding.checkboxSelectAll.isChecked = false
                cartViewModel.checkAll(0)
                showEmpty(true)
            }
        }
    }

    private fun buyProduct(requestBody : UpdateStockRequestBody){
        if (requestBody.data_stock.isEmpty()){
            Toast.makeText(this, "Sialhkan Centang Produk terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else {
            cartViewModel.deleteTrolleyChecked()
            val idList = id.toIntArray()
            cartViewModel.setBuyProduct(requestBody, preferences, this)
            val intent = Intent(this, SuccessPageActivity::class.java)
            intent.putExtra("data", idList)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

//            finish()
        }

    }


    private fun cekCheckBox(){
        val countCheck = cartViewModel.totalTrolleyCheck()
        val countTrolley = cartViewModel.totalTrolley()
        binding.checkboxSelectAll.isChecked = countCheck == countTrolley
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

}