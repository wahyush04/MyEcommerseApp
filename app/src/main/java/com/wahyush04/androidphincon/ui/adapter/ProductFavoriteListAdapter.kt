package com.wahyush04.androidphincon.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.databinding.ListProductBinding
import com.wahyush04.core.data.source.remote.response.product.DataListProduct
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//class ProductFavoriteListAdapter : RecyclerView.Adapter<ProductFavoriteListAdapter.ListViewHolder>() {
//
//    private var onItemClickCallback: OnItemClickCallback? = null
//
//    private val differCallback = object: DiffUtil.ItemCallback<DataListProduct>() {
//        override fun areItemsTheSame(oldItem: DataListProduct, newItem: DataListProduct): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: DataListProduct, newItem: DataListProduct): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this@ProductFavoriteListAdapter, differCallback)
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ProductFavoriteListAdapter.ListViewHolder {
//        val view = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ListViewHolder(view)
//    }
//
//    inner class ListViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root){
//        fun bind(data: DataListProduct){
//            binding.root.setOnClickListener {
//                onItemClickCallback?.onItemClicked(data)
//            }
//            val harga = data.harga
//            binding.apply {
//                Glide.with(itemView)
//                    .load(data.image)
//                    .centerCrop()
//                    .into(ivProduct)
//                tvProductName.text = data.name_product
//                tvPrice.text = formatRupiah(data.harga.toInt())
//                tvDate.text = formatDate(data.date)
//                ratingBar.rating = data.rate.toFloat()
//                tbFav.isChecked = true
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: ProductFavoriteListAdapter.ListViewHolder, position: Int) {
//        holder.bind(differ.currentList[position])
//    }
//
//    override fun getItemCount(): Int = differ.currentList.size
//
//    interface OnItemClickCallback{
//        fun onItemClicked(data: DataListProduct)
//    }
//
//    private fun formatRupiah(angka: Int): String {
//        val formatRupiah = DecimalFormat("Rp #,###")
//        return formatRupiah.format(angka)
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    private fun formatDate(date: String): String{
//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val inputDate = format.parse(date)
//        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
//        return dateFormat.format(inputDate!!)
//
//    }
//}

class ProductFavoriteListAdapter : RecyclerView.Adapter<ProductFavoriteListAdapter.ListViewHolder>() {
    private val listProduct = ArrayList<DataListProduct>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: List<DataListProduct>){
        listProduct.clear()
        listProduct.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DataListProduct){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(data.image)
                    .centerCrop()
                    .into(ivProduct)
                tvProductName.text = data.name_product
                tvPrice.text = formatRupiah(data.harga.toInt())
                tvDate.text = formatDate(data.date)
                ratingBar.rating = data.rate.toFloat()
                tbFav.isChecked = true
            }
            binding.tbFav.isEnabled = false
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int = listProduct.size

    interface OnItemClickCallback{
        fun onItemClicked(data: DataListProduct)
    }

    private fun formatRupiah(angka: Int): String {
        val formatRupiah = DecimalFormat("Rp #,###")
        return formatRupiah.format(angka)
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date: String): String{
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val inputDate = format.parse(date)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
        return dateFormat.format(inputDate!!)

    }


}