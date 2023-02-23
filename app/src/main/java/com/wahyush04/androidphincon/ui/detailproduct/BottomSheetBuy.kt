package com.wahyush04.androidphincon.ui.detailproduct

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.wahyush04.androidphincon.BaseFirebaseAnalytics
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.databinding.BottomSheetBuyBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.paymentmethod.PaymentMethodActivity
import com.wahyush04.androidphincon.ui.successpage.SuccessPageActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.remoteconfig.DataItem
import com.wahyush04.core.data.remoteconfig.PaymentMethod
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetBuy(private val data: DetailProductResponse?, private val payment : DataItem?): BottomSheetDialogFragment() {

    private var _binding: BottomSheetBuyBinding? = null
    private val binding get() =  _binding
    private var initiateHarga : Int? = 0
    private var totalHarga : Int? = 0
    private val formatRupiah = DecimalFormat("Rp #,###")
    @Inject
    lateinit var preferences: PreferenceHelper
    private val viewModel : DetailProductViewModel by viewModels()
    private lateinit var loadingDialog : LoadingDialog
    private var dataRemoteConfig: String? = null
    private val remoteConfig : FirebaseRemoteConfig = Firebase.remoteConfig
    private lateinit var id : MutableList<Int>
    private val firebaseAnalytics = BaseFirebaseAnalytics()

    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBuyBinding.inflate(inflater, container, false)
        initRemoteConfig()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity())
        preferences = PreferenceHelper(requireContext())
        initiateHarga = data?.success?.data?.harga?.toInt()
        id = mutableListOf<Int>()

        with(binding) {
            Glide.with(requireContext())
                .load(data?.success?.data?.image)
                .into(this!!.ivImageProduct)


            val hargaAwal = formatRupiah.format(initiateHarga)
            tvPriceProduct.text = formatRupiah.format(data?.success?.data?.harga?.toInt())
            tvStockProduct.text = "Stock : " + data?.success?.data?.stock.toString()
            tvBuyButton.text = "Buy Now - " + hargaAwal
        }

        setBuyButton()

        viewModel.quantity.observe(requireActivity()) {
            binding?.tvCount?.text = it.toString()
            if (it == data?.success?.data?.stock) {
                binding?.btnIncrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_disable)
                binding?.btnDecrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_active)
            } else if (it == 1){
                binding?.btnIncrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_active)
                binding?.btnDecrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_disable)
            }else{
                binding?.btnIncrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_active)
                binding?.btnDecrement?.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_rounded_active)
            }
        }

        if (payment == null){
            binding?.cvBuyButton?.setOnClickListener {
                //GA Slide 17 onCLickButtonBuyNowForPayment
                firebaseAnalytics.onClickButton(
                    "Detail Product",
                    binding!!.tvBuyButton.text.toString()
                )
                val intent = Intent(requireContext(), PaymentMethodActivity::class.java)
                intent.putExtra("id", data?.success?.data?.id?.toInt())
                intent.putExtra("isFrom", "bottomsheet")
                dismiss()
                startActivity(intent)
            }
        }else{
            binding?.sectionPaymentMethod?.setOnClickListener {
                //GA Slide 18  onClickIconBank
                firebaseAnalytics.onClickButton(
                    "Detail Product",
                    binding!!.tvPaymentMethod.text.toString()
                )
                val intent = Intent(requireContext(), PaymentMethodActivity::class.java)
                intent.putExtra("id", data?.success?.data?.id?.toInt())
                intent.putExtra("isFrom", "bottomsheet")
                dismiss()
                startActivity(intent)
            }
            binding?.sectionPaymentMethod?.visibility = View.VISIBLE
            binding?.tvPaymentMethod?.text = payment.name
            when (payment.id){
                "va_bca" ->
                    Glide.with(requireContext())
                        .load(R.drawable.bca)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "va_mandiri" ->
                    Glide.with(requireContext())
                        .load(R.drawable.mandiri)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "va_bri" ->
                    Glide.with(requireContext())
                        .load(R.drawable.bri)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "va_bni" ->
                    Glide.with(requireContext())
                        .load(R.drawable.bni)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "va_btn" ->
                    Glide.with(requireContext())
                        .load(R.drawable.btn)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "va_danamon" ->
                    Glide.with(requireContext())
                        .load(R.drawable.danamon)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "ewallet_gopay" ->
                    Glide.with(requireContext())
                        .load(R.drawable.gopay)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "ewallet_ovo" ->
                    Glide.with(requireContext())
                        .load(R.drawable.ovo)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
                "ewallet_dana" ->
                    Glide.with(requireContext())
                        .load(R.drawable.dana)
                        .fitCenter()
                        .into(binding!!.ivPaymentMethod)
            }
            binding?.cvBuyButton?.setOnClickListener {
                if (data?.success?.data?.stock == 1){
                    Toast.makeText(requireContext(), "Produk Out Of Stock", Toast.LENGTH_SHORT).show()
                }else{
                    buyProduct()
                }
            }
        }
    }

    private fun setBuyButton() {
        binding?.btnDecrement?.setOnClickListener {
            viewModel.decreaseQuantity()
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data?.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Buy Now - " + formatRupiah.format(totalHarga)
            //GA Slide 17 onCLickButtonPlusMinus
            firebaseAnalytics.onClickPlusMinus(
                "Detail Product",
                "-",
                binding?.tvCount?.text.toString().toInt(),
                data.success?.data?.id!!.toInt(),
                data.success?.data?.name_product.toString()
            )
        }
        binding?.btnIncrement?.setOnClickListener {
            viewModel.increaseQuantity(data?.success?.data?.stock)
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data?.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Buy Now - " + formatRupiah.format(totalHarga)
            //GA Slide 17 onCLickButtonPlusMinus
            firebaseAnalytics.onClickPlusMinus(
                "Detail Product",
                "+",
                binding?.tvCount?.text.toString().toInt(),
                data.success?.data?.id!!.toInt(),
                data.success?.data?.name_product.toString()
            )
        }
    }

    private fun buyProduct(){
        val sum = binding!!.tvCount.text.toString()
        totalHarga = (sum.toInt() * data?.success?.data?.harga!!.toInt())
        //GA Slide 18 onClickButtonBuyNow
        firebaseAnalytics.onClickIconBuyNow(
            "Detail Product",
            binding?.tvBuyButton?.text.toString(),
            data.success?.data?.id!!.toInt(),
            data.success!!.data?.name_product.toString(),
            data.success!!.data?.harga!!.toInt(),
            binding?.tvCount?.text.toString().toInt(),
            totalHarga!!.toDouble(),
            payment?.name.toString()

            )
        val idProduk = data.success?.data?.id
        if (idProduk != null) {
            id.add(idProduk.toInt())
        }
        val stock = binding?.tvCount?.text.toString().toInt()
        val idUser = preferences.getPreference(Constant.ID)
        val requestBody = UpdateStockRequestBody(idUser!!,listOf(DataStockItem(id[0].toString(), stock)))
        viewModel.buyProduct(requestBody).observe(viewLifecycleOwner){
            when (it){
                is Resource.Loading -> {
                    loadingDialog.startLoading()
                }
                is Resource.Success -> {
                    loadingDialog.stopLoading()
                    val intent = Intent(requireContext(), SuccessPageActivity::class.java)
                    Log.d("idProduk", id.toString())
                    intent.putExtra("data", id.toIntArray())
                    intent.putExtra("totalHarga", totalHarga)
                    intent.putExtra("idPayment", payment?.id)
                    intent.putExtra("namePayment", payment?.name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    dismiss()
                }
                is Resource.Error -> {
                    loadingDialog.stopLoading()
                    val err = it.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(err, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    val messageErr = errorResponse.error.message
                    Toast.makeText(requireContext(), messageErr, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(requireContext(), "Oops, Something when wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRemoteConfig(){
        val configSetting = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        remoteConfig.setConfigSettingsAsync(configSetting)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    dataRemoteConfig = remoteConfig.getString("payment_json")
                    val dataList = Gson().fromJson<ArrayList<PaymentMethod>>(dataRemoteConfig, object : TypeToken<ArrayList<PaymentMethod>>() {}.type)
                    Log.d("paymentMethod", dataList.toString())
                }else{
                    Toast.makeText(requireContext(), "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        //GA Slide 17 on Show Popup
        firebaseAnalytics.onPopupShow(
            "Detail Product",
            "show",
            data?.success?.data?.id!!.toInt(),
        )
    }
}
