package com.wahyush04.androidphincon.ui.detailproduct

import android.content.Intent
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
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.database.ProductEntity
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.formatRupiah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailProductBinding
    private lateinit var detailProductViewModel : DetailProductViewModel
    private lateinit var preferences: PreferenceHelper
    private val args: DetailProductActivityArgs? by navArgs()
    private var product: ProductEntity? = null
    private var isChecked = true
    private var idProduct : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showShimmer(true)

        supportActionBar?.hide()
        detailProductViewModel = ViewModelProvider(this)[DetailProductViewModel::class.java]
        preferences = PreferenceHelper(this)

        val intentID = intent.getIntExtra("id", 0)

        idProduct = intentID
        Log.d("idDetail", idProduct.toString())

        if (idProduct == 0){
            val data: Uri? = intent?.data
            val id = data?.getQueryParameter("id")
            if (id != null) {
                idProduct = id.toInt()
            }
        }

        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.setDetailProduct(preferences, this@DetailProductActivity, idProduct!!.toInt(), idUser)
        detailProductViewModel.getDetailProduct().observe(this){ data ->
            val id = data.success!!.data!!.id!!.toInt()
            val productName =  data.success!!.data!!.name_product.toString()
            val stock =  data.success!!.data!!.stock!!.toInt()
            val price = data.success!!.data!!.harga!!.toInt()
            val image = data.success!!.data!!.image.toString()

            binding.apply {
                tvProductTitle.text = data.success?.data?.name_product
                tvProductName.text = data.success?.data?.name_product
                tvProductPrice.text = data.success?.data?.harga?.let { formatRupiah(it.toInt()) }
                tvStockValue.text = data.success?.data?.stock.toString()
                tvSizeValue.text = data.success?.data?.size
                tvWeightValue.text = data.success?.data?.weight
                tvTypeValue.text = data.success?.data?.type
                tvDescription.text = data.success?.data?.desc
                ratingBar.rating = data.success?.data?.rate?.toFloat()!!
                viewPager.adapter = ImageViwPagerAdapter(data.success!!.data?.image_product)
                springDotsIndicator.attachTo(viewPager)
            }


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
                val bottomSheet = BottomSheet(data, "buy")
                bottomSheet.show(supportFragmentManager, "bottomSheet")
            }

            showShimmer(false)

            binding.btnTrolley.setOnClickListener {
                val bottomSheet = BottomSheet(data, "trolley")
                bottomSheet.show(supportFragmentManager, "bottomSheet")
//                product = ProductEntity(id, productName, price, price, stock, 1, image)
//                detailProductViewModel.insertTrolley(product as ProductEntity)
//                Toast.makeText(this, "User Berhasil Ditambah ke Trolley", Toast.LENGTH_SHORT).show()
            }

            binding.btnShare.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://wahyush04.com/deeplink?id=$id")
                startActivity(Intent.createChooser(shareIntent, "Share link using"))
            }

            binding.ivBack.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.tbFav.setOnClickListener {
            isChecked = !isChecked
            if (isChecked){
                detailProductViewModel.addFavorite(preferences, this, idProduct!!.toInt(), idUser)
                Toast.makeText(this, "Produk Berhasil Ditambah ke Favorite", Toast.LENGTH_SHORT).show()
            }else{
                detailProductViewModel.removeFavorite(preferences, this, idProduct!!.toInt(), idUser)
                Toast.makeText(this, "Produk berhasil dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.tbFav.isChecked = isChecked
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