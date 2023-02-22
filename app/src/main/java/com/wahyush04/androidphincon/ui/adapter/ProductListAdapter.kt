package com.wahyush04.androidphincon.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.databinding.ListProductBinding
import com.wahyush04.core.data.product.DataListProductPaging
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ProductListAdapter : PagingDataAdapter<DataListProductPaging, ProductListAdapter.ListViewHolder>(
    DIFF_CALLBACK
){
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = ListProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(val binding: ListProductBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DataListProductPaging){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(data.image)
                    .centerCrop()
                    .into(ivProduct)
                tvProductName.text = data.nameProduct
                tvPrice.text = formatRupiah(data.harga!!.toInt())
                tvDate.text = formatDate(data.date!!)
                ratingBar.rating = data.rate!!.toFloat()
                tbFav.visibility = View.GONE

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(data)
                }

            }
            Log.d("paging", "holderOK")
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            Log.d("paging", "bindOK")
        }
    }


    interface OnItemClickCallback{
        fun onItemClicked(data: DataListProductPaging)
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

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataListProductPaging>() {
            override fun areItemsTheSame(oldItem: DataListProductPaging, newItem: DataListProductPaging): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataListProductPaging, newItem: DataListProductPaging): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}