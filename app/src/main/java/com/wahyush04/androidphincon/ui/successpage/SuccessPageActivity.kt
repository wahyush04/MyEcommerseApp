package com.wahyush04.androidphincon.ui.successpage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.databinding.ActivitySuccessPageBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class SuccessPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySuccessPageBinding
    private val successPageViewModel : SuccessPageViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceHelper
    private var idList : List<Int>? = null
    private var id : Int = 0
    private lateinit var loadingDialog: LoadingDialog
    private val formatRupiah = DecimalFormat("Rp #,###")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        loadingDialog = LoadingDialog(this@SuccessPageActivity)
        val idListIntArray = intent.extras?.getIntArray("data")
        Log.d("idbuy", idListIntArray.toString())
        val totalHarga = intent.getIntExtra("totalHarga", 0)
        val idPayment = intent.getStringExtra("idPayment")
        val namePayment = intent.getStringExtra("namePayment")

        binding.tvTotalPriceValue.text = formatRupiah.format(totalHarga)
        binding.tvPaymentMethod.text = namePayment.toString()
        when (idPayment){
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

        idList = idListIntArray?.toList()
        Log.d("list", idList.toString())
        if (idList == null){
            id = intent.getStringExtra("data")!!.toInt()
            Log.d("idIntent", id.toString())
        }

        binding.btnSubmitRating.setOnClickListener {
            val rating =  binding.ratingBar.rating
            Log.d("idSuccess", id.toString())
            if (idList !== null){
                Log.d("idSuccess", id.toString())
                for ((index, item) in idList!!.withIndex()){
                    Log.d("idSuccessItem", id.toString())
                    successPageViewModel.updateRating(item, rating.toString()).observe(this@SuccessPageActivity){
                        when (it) {
                            is Resource.Loading -> {
                                loadingDialog.startLoading()
                            }
                            is Resource.Success -> {
                                loadingDialog.stopLoading()
                                if (index == idList!!.lastIndex) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                            }
                            is Resource.Error -> {
                                loadingDialog.stopLoading()
                                Toast.makeText(this@SuccessPageActivity, "Oops, Something when wrong", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                loadingDialog.stopLoading()
                                Toast.makeText(this@SuccessPageActivity, "Oops, Something when wrong", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this@SuccessPageActivity, "No data", Toast.LENGTH_SHORT).show()
            }

        }
        successPageViewModel.deleteTrolleyChecked()

    }
}