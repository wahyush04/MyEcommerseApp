package com.wahyush04.androidphincon.ui.detailproduct

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.BottomSheetBuyBinding
import com.wahyush04.androidphincon.ui.main.dashboard.DashboardViewModel
import com.wahyush04.androidphincon.ui.successpage.SuccessPageActivity
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.database.ProductEntity
import com.wahyush04.core.helper.PreferenceHelper
import java.text.DecimalFormat


class BottomSheet(private val data: DetailProductResponse,private val from : String): BottomSheetDialogFragment() {

    private var _binding: BottomSheetBuyBinding? = null
    private val binding get() =  _binding
    private var initiateHarga : Int? = 0
    private var totalHarga : Int? = 0
    private val formatRupiah = DecimalFormat("Rp #,###")
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var detailProductViewModel : DetailProductViewModel

    private val bottomSheetViewModel: BuyBottomSheetViewModel by viewModels()

    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetBuyBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailProductViewModel =
            ViewModelProvider(this)[DetailProductViewModel::class.java]

        sharedPreferences = PreferenceHelper(requireContext())
        initiateHarga = data.success?.data?.harga?.toInt()

        with(binding) {
            Glide.with(requireContext())
                .load(data.success?.data?.image)
                .into(this!!.ivImageProduct)


            val hargaAwal = formatRupiah.format(initiateHarga)
            tvPriceProduct.text = formatRupiah.format(data.success?.data?.harga?.toInt())
            tvStockProduct.text = "Stock : " + data.success?.data?.stock.toString()
            tvBuyButton.text = "Buy Now - " + hargaAwal
        }


        setBuyButton()

        bottomSheetViewModel.quantity.observe(requireActivity()) {
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

        binding?.cvBuyButton?.setOnClickListener {
            if (from == "buy") {
                buyProduct()
            }else if (from == "trolley"){
                addToTrolley()
            }

        }
    }

    private fun setBuyButton() {
        binding?.btnDecrement?.setOnClickListener {
            bottomSheetViewModel.decreaseQuantity()
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Buy Now - " + formatRupiah.format(totalHarga)
        }
        binding?.btnIncrement?.setOnClickListener {
            bottomSheetViewModel.increaseQuantity(data.success?.data?.stock)
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Buy Now - " + formatRupiah.format(totalHarga)
        }
    }

    private fun buyProduct(){
        val stock = binding?.tvCount?.text.toString().toInt()
        val id = data.success?.data?.id
        bottomSheetViewModel.setBuyProduct(id, stock, sharedPreferences, requireContext())
        bottomSheetViewModel.getUpdateStockresponse().observe(viewLifecycleOwner){
            if (it != null){
                if (it.success.status == 201){
                    val intent = Intent(requireContext(), SuccessPageActivity::class.java)
                    intent.putExtra("data", id)
                    startActivity(intent)
                }else{
                    Toast.makeText(requireContext(), "Gagal Melakukan Pembelian", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addToTrolley(){
        val id = data.success?.data?.id
        val productName = data.success?.data?.name_product
        val price = data.success?.data?.harga?.toInt()
        val stock = binding?.tvCount?.text.toString().toInt()
        val stockbuy = binding?.tvCount?.text.toString().toInt()
        val totalHarga = totalHarga
        val image = data.success?.data?.image
                val product = ProductEntity(id!!.toInt(), productName!!, price!!, totalHarga!!.toInt(), stock, stockbuy, image.toString())
                detailProductViewModel.insertTrolley(product as ProductEntity)
                Toast.makeText(requireContext(), "Data Berhasil Ditambah ke Trolley", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
