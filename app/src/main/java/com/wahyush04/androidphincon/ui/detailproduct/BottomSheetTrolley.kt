package com.wahyush04.androidphincon.ui.detailproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.core.data.source.local.entity.ProductEntity
import com.wahyush04.androidphincon.databinding.BottomSheetBuyBinding
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class BottomSheetTrolley(private val data: DetailProductResponse): BottomSheetDialogFragment() {

    private var _binding: BottomSheetBuyBinding? = null
    private val binding get() =  _binding
    private var initiateHarga : Int? = 0
    private var totalHarga : Int? = 0
    private val formatRupiah = DecimalFormat("Rp #,###")
    private lateinit var sharedPreferences: PreferenceHelper

    private val viewModel: DetailProductViewModel by viewModels()

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


        sharedPreferences = PreferenceHelper(requireContext())
        initiateHarga = data.success?.data?.harga?.toInt()

        with(binding) {
            Glide.with(requireContext())
                .load(data.success?.data?.image)
                .into(this!!.ivImageProduct)


            val hargaAwal = formatRupiah.format(initiateHarga)
            tvPriceProduct.text = formatRupiah.format(data.success?.data?.harga?.toInt())
            tvStockProduct.text = "Stock : " + data.success?.data?.stock.toString()
            tvBuyButton.text = "Add Trolley - " + hargaAwal
        }

        setBuyButton()

        viewModel.quantity.observe(requireActivity()) {
            binding?.tvCount?.text = it.toString()
            if (it == data.success?.data?.stock) {
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
            val isTrolley = viewModel.isTrolley(data.success?.data?.id.toString().toInt())
            if (isTrolley == 1){
                Toast.makeText(requireContext(), "Product Already in Trolley", Toast.LENGTH_SHORT).show()
            } else if (data.success?.data?.stock == 1)
                Toast.makeText(requireContext(), "Out of Stock", Toast.LENGTH_SHORT).show()
            else{
                addToTrolley()
                dismiss()
            }
        }
    }

    private fun setBuyButton() {
        binding?.btnDecrement?.setOnClickListener {
            viewModel.decreaseQuantity()
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Add Trolley - " + formatRupiah.format(totalHarga)
        }
        binding?.btnIncrement?.setOnClickListener {
            viewModel.increaseQuantity(data.success?.data?.stock)
            val sum = binding!!.tvCount.text.toString()
            totalHarga = (sum.toInt() * data.success?.data?.harga!!.toInt())
            binding!!.tvBuyButton.text = "Add Trolley - " + formatRupiah.format(totalHarga)
        }
    }

    private fun addToTrolley(){
        val id = data.success?.data?.id
        val productName = data.success?.data?.name_product
        val price = data.success?.data?.harga?.toInt()
        val stock = data.success!!.data!!.stock!!.toInt()
        val stockbuy = binding?.tvCount?.text.toString().toInt()
        val totalHarga = price?.times(stockbuy)
        val image = data.success?.data?.image
        val product = ProductEntity(id!!.toInt(), productName!!, price!!, totalHarga!!.toInt(), stock, stockbuy, image.toString(), 0)
        viewModel.insertTrolley(product)
        Toast.makeText(requireContext(), "Data Berhasil Ditambah ke Trolley", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
