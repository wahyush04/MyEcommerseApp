package com.wahyush04.androidphincon.ui.paymentmethod

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wahyush04.androidphincon.databinding.ActivityPaymentMethodBinding
import com.wahyush04.androidphincon.ui.cart.CartActivity
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.core.data.remoteconfig.PaymentMethod

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentMethodBinding
    private var dataRemoteConfig: String? = null
    private lateinit var adapter : PaymentMethodAdapterHeader
    private val remoteConfig : FirebaseRemoteConfig = Firebase.remoteConfig
    private var dataList : List<PaymentMethod>? = null
    private var isFrom : String? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val intentID = intent.getIntExtra("id", 0)
        isFrom = intent.getStringExtra("isFrom")

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        initRemoteConfig()
        adapter = PaymentMethodAdapterHeader(
            {
                if (isFrom == "bottomsheet"){
                    val intent = Intent(this@PaymentMethodActivity, DetailProductActivity::class.java)
                    intent.putExtra("payment_method", it)
                    intent.putExtra("id", intentID)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else if (isFrom == "trolley"){
                    val intent = Intent(this@PaymentMethodActivity, CartActivity::class.java)
                    intent.putExtra("payment_method", it)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            },
            {
                Toast.makeText(this@PaymentMethodActivity, it.type.toString()+" Collpased", Toast.LENGTH_SHORT).show()
            },
            this@PaymentMethodActivity
        )
        adapter.notifyDataSetChanged()

    }

    private fun initRemoteConfig() {
        val configSetting = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        remoteConfig.setConfigSettingsAsync(configSetting)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    dataRemoteConfig = remoteConfig.getString("payment_json")
                    dataList = Gson().fromJson<List<PaymentMethod>>(
                        dataRemoteConfig,
                        object : TypeToken<List<PaymentMethod>>() {}.type
                    )
                    binding.rvPaymentMethodParent.setHasFixedSize(true)
                    binding.rvPaymentMethodParent.adapter = adapter
                    val sorted = dataList as List<PaymentMethod>
                    adapter.setList(sorted.sortedBy { it.order })
                }   else {
                    Toast.makeText(
                        this@PaymentMethodActivity, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}