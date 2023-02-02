package com.wahyush04.androidphincon.ui.detailproduct

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.wahyush04.androidphincon.databinding.ActivityDetailProductBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailProductBinding
    private lateinit var detailProductViewModel : DetailProductViewModel
    private lateinit var preferences: PreferenceHelper
    private var isChecked = true
    private var idProduct : Int? = null
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showShimmer(true)

        supportActionBar?.hide()
        detailProductViewModel =
            ViewModelProvider(this)[DetailProductViewModel::class.java]
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

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            getData()
            swipeRefreshLayout.isRefreshing = false
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


    private fun setData(){
//        showShimmer(true)
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.getDetailProduct().observe(this){ data ->
            val id = data.success!!.data!!.id!!.toInt()
            val productName =  data.success!!.data!!.name_product.toString()
            val stock =  data.success!!.data!!.stock!!.toInt()
            val weight : String? =  data.success?.data?.weight
            val size : String? =  data.success?.data?.size
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
                Glide.with(applicationContext)
                    .load(image)
                    .centerCrop()
                    .into(tempImage!!)
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


            binding.btnTrolley.setOnClickListener {
                val bottomSheet = BottomSheetTrolley(data, "trolley")
                bottomSheet.show(supportFragmentManager, "bottomSheet")

            }

            binding.btnShare.setOnClickListener {
                shareDeepLink(productName, stock.toString(), weight.toString(), size.toString(), "https://wahyush04.com/deeplink?id=$id", image)
            }

            binding.ivBack.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            showShimmer(false)
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

    private fun getData(){
        showShimmer(true)
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.setDetailProduct(preferences, this@DetailProductActivity, idProduct!!.toInt(), idUser)
    }

    private fun shareDeepLink(name : String, stock : String, weight: String, size : String, link : String, image : String){

        Picasso.get().load(image).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Name : $name\nStock : $stock\nWeight : $weight\nSize : $size\nLink : $link"
                )
                intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(bitmap))
                startActivity(Intent.createChooser(intent, "Share To"))
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.v("IMG Downloader", "Bitmap Failed...");
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.v("IMG Downloader", "Bitmap Preparing Load...");
            }
        })

    }


    private fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(this.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")

            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }

    override fun onPause() {
        super.onPause()
        showShimmer(false)
    }

    override fun onStart() {
        super.onStart()
        showShimmer(true)
        coroutineScope.launch {
            val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
            detailProductViewModel.setDetailProduct(preferences, this@DetailProductActivity, idProduct!!.toInt(), idUser)
        }
        setData()
    }
}