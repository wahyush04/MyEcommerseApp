package com.wahyush04.androidphincon.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.androidphincon.databinding.ListProductBinding
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ListViewHolder>() {
    private val listProduct = ArrayList<DataListProduct>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<DataListProduct>){
        listProduct.clear()
        listProduct.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapter.ListViewHolder {
        val view = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DataListProduct){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
            val harga = data.harga
            binding.apply {
                Glide.with(itemView)
                    .load(data.image)
                    .centerCrop()
                    .into(ivProduct)
                tvProductName.text = data.name_product
                tvPrice.text = formatRupiah(data.harga.toInt())
                tvDate.text = formatDate(data.date)
                ratingBar.rating = data.rate.toFloat()
            }
        }
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ListViewHolder, position: Int) {
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