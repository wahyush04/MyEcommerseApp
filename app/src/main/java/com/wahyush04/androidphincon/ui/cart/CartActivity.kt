package com.wahyush04.androidphincon.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.BaseFirebaseAnalytics
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.databinding.ActivityCartBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.ui.paymentmethod.PaymentMethodActivity
import com.wahyush04.androidphincon.ui.successpage.SuccessPageActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.remoteconfig.DataItem
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatterIdr
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartListAdapter
    private val cartViewModel : CartViewModel by viewModels()
    private lateinit var preferences : PreferenceHelper
    private lateinit var mutableListItem : MutableList<DataStockItem>
    private lateinit var id : MutableList<Int>
    private lateinit var loadingDialog : LoadingDialog
    private var paymentMethod : DataItem? = null
    private val firebaseAnalytics = BaseFirebaseAnalytics()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        paymentMethod = intent.getParcelableExtra<DataItem>("payment_method")
        loadingDialog = LoadingDialog(this@CartActivity)
        preferences = PreferenceHelper(this)
        mutableListItem = mutableListOf()
        id = mutableListOf<Int>()

        doActionClicked()

        binding.btnBack.setOnClickListener {
            //GA Slide 22 OnClickBackIcon
            firebaseAnalytics.onClickButton(
                "Trolley",
                "Back Icon"
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.checkboxSelectAll.isChecked = preferences.getIsCheck(Constant.ISCHECK)

        binding.checkboxSelectAll.setOnClickListener {
            if (!cekCheckBox()){
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
            binding.checkboxSelectAll.isChecked = cekCheckBox()
            preferences.putCheck(cekCheckBox())
        }

        if (paymentMethod != null){
            binding.sectionPaymentMethod.visibility = View.VISIBLE
            when (paymentMethod!!.id){
                "va_bca" ->
                    Glide.with(this)
                        .load(R.drawable.bca)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_mandiri" ->
                    Glide.with(this)
                        .load(R.drawable.mandiri)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_bri" ->
                    Glide.with(this)
                        .load(R.drawable.bri)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_bni" ->
                    Glide.with(this)
                        .load(R.drawable.bni)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_btn" ->
                    Glide.with(this)
                        .load(R.drawable.btn)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_danamon" ->
                    Glide.with(this)
                        .load(R.drawable.danamon)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_gopay" ->
                    Glide.with(this)
                        .load(R.drawable.gopay)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_ovo" ->
                    Glide.with(this)
                        .load(R.drawable.ovo)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_dana" ->
                    Glide.with(this)
                        .load(R.drawable.dana)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
            }
        }


        if (paymentMethod != null){
            binding.ivPaymentMethod.visibility = View.VISIBLE
            binding.sectionPaymentMethod.setOnClickListener {
                //GA Slide 23 onCLickIconBank
                firebaseAnalytics.onClickButton("Trolley", binding.tvPaymentMethod.text.toString())
                val intent = Intent(this, PaymentMethodActivity::class.java)
                intent.putExtra("isFrom", "trolley")
                startActivity(intent)
            }
            binding.tvPaymentMethod.text = paymentMethod!!.name
            binding.btnBuy.setOnClickListener {
                val idUser =  preferences.getPreference(Constant.ID)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()

                val checkedTrolley =  cartViewModel.getTrolleyChecked()
                for (item in checkedTrolley){
                    mutableListItem.add(DataStockItem(item.id.toString(), item.stock_buy))
                    id.add(item.id)
                }

                val listListRequestItem: List<DataStockItem> = mutableListItem.toList()

                val requestBody = UpdateStockRequestBody(idUser!!,listListRequestItem)
                buyProduct(requestBody)
                showEmpty(false)
                preferences.putCheck(false)

            }
        }else{
            binding.btnBuy.setOnClickListener {
                //GA SLide 22 onClickBuyButton
                firebaseAnalytics.onClickButton(
                    "Trolley",
                    "Buy"
                )
                val intent = Intent(this, PaymentMethodActivity::class.java)
                intent.putExtra("isFrom", "trolley")
                startActivity(intent)
            }

        }

        val result = cartViewModel.getTotalHarga()
        binding.tvTotalPrice.text = result.toString().formatterIdr()
    }


    private fun doActionClicked() {
        adapter = CartListAdapter(
            {
                //GA Slide 22 onClickDelete
                firebaseAnalytics.onCLickDeleteTrolley(
                    "Trolley",
                    "Delete Icon",
                    it.id,
                    it.name_product,
                )
                cartViewModel.deleteCart(it)
                val result = cartViewModel.getTotalHarga()
                binding.tvTotalPrice.text = result.toString().formatterIdr()
                cekCheckBox()
            },
            {
                //GA Slide 22 onSelectCheckBox
                firebaseAnalytics.onSelectCheckBox(
                    "Trolley",
                    it.id,
                    it.name_product
                )
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
                val maxQuantity = it.stock
                val newTotalHarga = it.total_harga + it.harga
                if (quantity == maxQuantity){
                    Toast.makeText(this@CartActivity, "Out of Stock", Toast.LENGTH_SHORT).show()
                }else{
                    cartViewModel.updateQuantity((quantity + 1), id,newTotalHarga)
                }
                //GA Slide 22 onClickPlusMinus
                firebaseAnalytics.onClickPlusMinus(
                    "Trolley",
                    "+",
                    quantity,
                    id,
                    it.name_product
                )

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
                //GA Slide 22 onClickPlusMinus
                firebaseAnalytics.onClickPlusMinus(
                    "Trolley",
                    "-",
                    quantity,
                    id,
                    it.name_product
                )
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
        cartViewModel.getTrolley().observe(this@CartActivity) {
            if (it.isNotEmpty()) {
                showEmpty(false)
                adapter.setData(it)
            } else {
                binding.checkboxSelectAll.isChecked = false
                preferences.putCheck(false)
                cartViewModel.checkAll(0)
                showEmpty(true)
            }
        }
    }

    private fun buyProduct(requestBody : UpdateStockRequestBody) {
        val totalHarga = cartViewModel.getTotalHarga()
        //GA Slide 23 onClickBuy Button
        firebaseAnalytics.onClickBuyTrolley(
            "Trolley",
            "Buy",
            totalHarga.toDouble(),
            paymentMethod?.name.toString()
        )
        if (requestBody.data_stock.isEmpty()) {
            Toast.makeText(this, "Sialahkan Centang Produk terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
        } else {
            val idList = id.toIntArray()
            cartViewModel.buyProduct(requestBody).observe(this@CartActivity) {
                when (it) {
                    is Resource.Loading -> {
                        loadingDialog.startLoading()
                    }
                    is Resource.Success -> {
                        loadingDialog.stopLoading()
                        val totalHarga = cartViewModel.getTotalHarga()
                        val intent = Intent(this, SuccessPageActivity::class.java)
                        intent.putExtra("data", idList)
                        intent.putExtra("totalHarga", totalHarga)
                        intent.putExtra("idPayment", paymentMethod?.id)
                        intent.putExtra("namePayment", paymentMethod?.name)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        loadingDialog.stopLoading()
                        val err = it.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(err, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponse::class.java)
                        val messageErr = errorResponse.error.message
                        Toast.makeText(this@CartActivity, messageErr, Toast.LENGTH_SHORT).show()
                        if (it.errorCode == 429){
                            Toast.makeText(this, "Too Many Request", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        loadingDialog.stopLoading()
                        Toast.makeText(this, "Oops, Something when wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun cekCheckBox() : Boolean{
        val countCheck = cartViewModel.countItemsCheckedTrolley()
        val countTrolley = cartViewModel.totalTrolley()
        binding.checkboxSelectAll.isChecked = countCheck == countTrolley
        preferences.putCheck(countCheck == countTrolley)
        return countCheck == countTrolley
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
        //GA SLide 22 onLoadScreen
        firebaseAnalytics.onLoadScreen("Trolley", this.javaClass.simpleName)
    }
}