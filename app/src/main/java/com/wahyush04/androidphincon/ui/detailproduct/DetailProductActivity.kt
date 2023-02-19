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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.wahyush04.androidphincon.databinding.ActivityDetailProductBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.ui.main.adapter.OtherProductAdapter
import com.wahyush04.core.Constant
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class DetailProductActivity : AppCompatActivity(), ImageViewPagerAdapter.OnPageClickListener {
    private lateinit var binding : ActivityDetailProductBinding
    private val detailProductViewModel : DetailProductViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceHelper
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
            setData()
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
        idProduct?.let { it ->
            detailProductViewModel.detailProduct(it, idUser).observe(this){ data ->
                when (data){
                    is Resource.Loading -> {
                        showShimmer(true)
                    }
                    is Resource.Success -> {
                        showShimmer(false)
                        val id = data.data?.success?.data?.id?.toInt()
                        val productName =  data.data?.success?.data?.name_product.toString()
                        val stock =  data.data?.success?.data?.stock
                        val weight : String? =  data.data?.success?.data?.weight
                        val type : String? =  data.data?.success?.data?.type
                        val size : String? =  data.data?.success?.data?.size
                        val rating : Int? =  data.data?.success?.data?.rate
                        val description : String? =  data.data?.success?.data?.desc
                        val price = data.data?.success?.data?.harga?.toInt()
                        val image = data.data?.success?.data?.image.toString()
                        val imageListProduk = data.data?.success?.data?.image_product

                        binding.apply {
                            tvProductTitle.text = productName
                            tvProductName.text = productName
                            tvProductPrice.text = price?.let { it1 -> formatRupiah(it1) }
                            tvStockValue.text = stock.toString()
                            tvSizeValue.text = size
                            tvWeightValue.text = weight
                            tvTypeValue.text = type
                            tvDescription.text = description
                            if (rating != null) {
                                ratingBar.rating = rating.toFloat()
                            }
                            viewPager.adapter = ImageViewPagerAdapter(imageListProduk, this@DetailProductActivity)
                            springDotsIndicator.attachTo(viewPager)
                        }


                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(Dispatchers.Main){
                                if (data.data!!.success?.data?.isFavorite != null){
                                    if (data.data!!.success?.data?.isFavorite == true){
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
                            data.data?.let { it1 -> BottomSheet(it1, "buy") }
                                ?.show(supportFragmentManager, "bottomSheet")
                        }


                        binding.btnTrolley.setOnClickListener {
                            data.data?.let { it1 -> BottomSheetTrolley(it1, "trolley") }
                                ?.show(supportFragmentManager, "bottomSheet")

                        }

                        binding.btnShare.setOnClickListener {
                            shareDeepLink(productName, stock.toString(), weight.toString(), size.toString(), "https://wahyush04.com/deeplink?id=$id", image)
                        }

                        binding.ivBack.setOnClickListener {
                            startActivity(Intent(this, MainActivity::class.java))
                        }

                    }
                    else -> {
                        Toast.makeText(this@DetailProductActivity, "Ooops, Something when wrong", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        binding.tbFav.setOnClickListener {
            isChecked = !isChecked
            if (isChecked){
                idProduct?.let { it1 -> detailProductViewModel.addFavorite(it1, idUser) }
                Toast.makeText(this, "Produk Berhasil Ditambah ke Favorite", Toast.LENGTH_SHORT).show()
            }else{
                idProduct?.let { it1 -> detailProductViewModel.removeFavorite(it1, idUser) }
                Toast.makeText(this, "Produk berhasil dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.tbFav.isChecked = isChecked
        }


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
        detailProductViewModel.getOtherProduk(idUser).observe(this){data ->
            if (data != null){
                data.data?.success?.let { adapterOtherProduct.setList(it.data) }
                if (adapterOtherProduct.itemCount < 1){
                    binding.sectionOtherProductHeader.visibility = View.GONE
                    binding.sectionLine1.visibility = View.GONE
                }
            }else{
                Toast.makeText(this, "No Other Data Product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHistoryProduct(){
        val idUser : Int = preferences.getPreference(Constant.ID)!!.toInt()
        detailProductViewModel.getHistoryProduk(idUser).observe(this){data ->
            if (data != null){
                data.data?.success?.let { adapterHistoryProduct.setList(it.data) }
                if (adapterOtherProduct.itemCount < 1){
                    binding.sectionOtherProductHeader.visibility = View.GONE
                    binding.sectionLine1.visibility = View.GONE
                }
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
        setData()
    }

    override fun onClick(image: String) {
        val imageDialog = ImageViewDialog(image)
        imageDialog.show(supportFragmentManager, "bottomSheet")
    }

}