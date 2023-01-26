package com.wahyush04.androidphincon.ui.cart

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.CartItemBinding
import com.wahyush04.core.database.ProductEntity
import com.wahyush04.core.helper.formatRupiah
import com.wahyush04.core.helper.formatterIdr

class CartListAdapter(
    private val onDeleteItem: (ProductEntity) -> Unit
) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    private var listData = ArrayList<ProductEntity>()
    var onItemClick: ((ProductEntity) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<ProductEntity>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CartItemBinding.bind(itemView)
        fun bind(data: ProductEntity) {
            binding.apply {
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.image)
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .into(imgProduct)
                tvTittleProduct.isSelected = true
                tvTittleProduct.text = data.name_product
                tvPriceProduct.text = formatRupiah(data.harga)
                tvQuantity.text = data.stockbuy.toString()
                btnDelete.setOnClickListener {
                    onDeleteItem(data)
                }
                addQuantity.setOnClickListener {
                    CartActivity().addQuantity()
                    tvQuantity.text
                }
                decreaseQuantity.setOnClickListener {
                    CartActivity().decQuantity()
                }
            }
        }
    }
}