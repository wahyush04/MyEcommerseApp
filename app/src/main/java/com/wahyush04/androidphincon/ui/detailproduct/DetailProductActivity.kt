package com.wahyush04.androidphincon.ui.detailproduct

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityDetailProductBinding
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailProductBinding
    private lateinit var detailProductViewModel : DetailProductViewModel
    private lateinit var preferences: PreferenceHelper
    private val args: DetailProductActivityArgs by navArgs()
    private var isChecked = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showShimmer(true)

        val data = intent.data
        if (data != null) {
            val param1 = data.getQueryParameter("idproduct")
            Log.d("datadeeplink", param1.toString())
        }

        supportActionBar?.hide()
        detailProductViewModel = ViewModelProvider(this)[DetailProductViewModel::class.java]
        preferences = PreferenceHelper(this)

        val idProduct = args.id
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.setDetailProduct(preferences, this@DetailProductActivity, idProduct, idUser)
        detailProductViewModel.getDetailProduct().observe(this){ data ->
            binding.tvProductTitle.text = data.success?.data?.name_product
            binding.tvProductName.text = data.success?.data?.name_product
            binding.tvProductPrice.text = data.success?.data?.harga?.let { formatRupiah(it.toInt()) }
            binding.tvStockValue.text = data.success?.data?.stock.toString()
            binding.tvSizeValue.text = data.success?.data?.size
            binding.tvWeightValue.text = data.success?.data?.weight
            binding.tvTypeValue.text = data.success?.data?.type
            binding.tvDescription.text = data.success?.data?.desc
            binding.ratingBar.rating = data.success?.data?.rate?.toFloat()!!
            binding.viewPager.adapter = ImageViwPagerAdapter(data.success!!.data?.image_product)

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    if (data.success!!.data?.isFavorite != null){
                        if (data.success!!.data?.isFavorite == true){
                            binding.tbFav.isChecked = true
                            isChecked = true
                        }else{
                            binding.tbFav.isChecked = false
                            isChecked = false
                        }
                    }
                }
            }

            binding.btnBuy.setOnClickListener {
                val bottomSheet = BottomSheet(data)
                bottomSheet.show(supportFragmentManager, "bottomSheet")
            }

            showShimmer(false)
        }

        binding.tbFav.setOnClickListener {
            showShimmer(true)
            isChecked = !isChecked
            if (isChecked){
                detailProductViewModel.addFavorite(preferences, this, idProduct, idUser)
                Toast.makeText(this, "Produk Berhasil Ditambah ke Favorite", Toast.LENGTH_SHORT).show()
            }else{
                detailProductViewModel.removeFavorite(preferences, this, idProduct, idUser)
                Toast.makeText(this, "Produk berhasil dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.tbFav.isChecked = isChecked
            showShimmer(true)
        }

    }

    private fun formatRupiah(angka: Int): String {
        val formatRupiah = DecimalFormat("Rp #,###")
        return formatRupiah.format(angka)
    }

    private fun showShimmer(state : Boolean){
        if (state){
            binding.scrollView.visibility = View.GONE
            binding.shimmerDetail.startShimmer()
            binding.shimmerDetail.visibility = View.VISIBLE
            binding.botNavLayout.visibility = View.GONE
        }else{
            binding.scrollView.visibility = View.VISIBLE
            binding.shimmerDetail.visibility = View.GONE
            binding.shimmerDetail.stopShimmer()
            binding.botNavLayout.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        showShimmer(false)
    }
}