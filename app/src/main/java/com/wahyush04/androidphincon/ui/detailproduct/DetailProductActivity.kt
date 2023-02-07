package com.wahyush04.androidphincon.ui.detailproduct

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.wahyush04.androidphincon.databinding.ActivityDetailProductBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.ui.main.adapter.OtherProductAdapter
import com.wahyush04.core.Constant
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class DetailProductActivity : AppCompatActivity(), ImageViewPagerAdapter.OnPageClickListener {
    private lateinit var binding : ActivityDetailProductBinding
    private lateinit var detailProductViewModel : DetailProductViewModel
    private lateinit var preferences: PreferenceHelper
    private var isChecked = true
    private var idProduct : Int? = null
    private lateinit var adapterOtherProduct : OtherProductAdapter
    private lateinit var adapterHistoryProduct : OtherProductAdapter
    private lateinit var viewPagerAdapter : ImageViewPagerAdapter
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showShimmer(true)

        supportActionBar?.hide()
        adapterOtherProduct = OtherProductAdapter()
        adapterOtherProduct.notifyDataSetChanged()
        adapterHistoryProduct = OtherProductAdapter()
        adapterHistoryProduct.notifyDataSetChanged()


        binding.rvOtherProduct.setHasFixedSize(true)
        binding.rvOtherProduct.adapter = adapterOtherProduct
        binding.rvSearchHistory.setHasFixedSize(true)
        binding.rvSearchHistory.adapter = adapterHistoryProduct


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

        setOtherProduct()
        setHistoryProduct()

        adapterOtherProduct.setOnItemClickCallback(object : OtherProductAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                val intent = Intent(this@DetailProductActivity, DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }
        })

        adapterHistoryProduct.setOnItemClickCallback(object : OtherProductAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                val intent = Intent(this@DetailProductActivity, DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }
        })
    }

    private fun formatRupiah(angka: Int): String {
        val formatRupiah = DecimalFormat("Rp #,###")
        return formatRupiah.format(angka)
    }

    private fun showShimmer(state : Boolean){
        if (state){
            binding.swipeRefreshLayout.visibility = View.GONE
            binding.scrollView.visibility = View.GONE
            binding.shimmerDetail.startShimmer()
            binding.shimmerDetail.visibility = View.VISIBLE
            binding.botNavLayout.visibility = View.GONE
        }else{
            binding.swipeRefreshLayout.visibility = View.VISIBLE
            binding.scrollView.visibility = View.VISIBLE
            binding.shimmerDetail.visibility = View.GONE
            binding.shimmerDetail.stopShimmer()
            binding.botNavLayout.visibility = View.VISIBLE
        }
    }


    private fun setData(){
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
                viewPager.adapter = ImageViewPagerAdapter(data.success!!.data!!.image_product, this@DetailProductActivity)
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
                    "Name : ${name}\nStock : ${stock}\nWeight : ${weight}\nSize : ${size}\n $link"
                )

                val path = MediaStore.Images.Media.insertImage(
                    contentResolver,
                    bitmap,
                    "image desc",
                    null
                )

                val uri = Uri.parse(path)

                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Share To"))
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.v("IMG Downloader", "Bitmap Failed...")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.v("IMG Downloader", "Bitmap Preparing Load...")
            }
        })
    }


    private fun setOtherProduct(){
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.setOtherProduct(idUser, this, preferences)
        detailProductViewModel.getOtherProduct().observe(this){data ->
            if (data != null){
                adapterOtherProduct.setList(data)
            }else{
                Toast.makeText(this, "No Other Data Product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHistoryProduct(){
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.setHistoryProduct(idUser, this, preferences)
        detailProductViewModel.getHistoryProduct().observe(this){data ->
            if (data != null){
                adapterHistoryProduct.setList(data)
            }else{
                Toast.makeText(this, "No History Data Product", Toast.LENGTH_SHORT).show()
            }
        }
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



    override fun onClick(image: String) {
        val imageDialog = ImageViewDialog(image)
        imageDialog.show(supportFragmentManager, "bottomSheet")
    }

}