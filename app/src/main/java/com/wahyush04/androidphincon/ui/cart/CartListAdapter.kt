package com.wahyush04.androidphincon.ui.cart

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.CartItemBinding
import com.wahyush04.core.database.ProductEntity
import com.wahyush04.core.helper.formatRupiah
import com.wahyush04.core.helper.formatterIdr

@Suppress("DEPRECATION")
class CartListAdapter(
    private val onDeleteItem: (ProductEntity) -> Unit,
    private val onCheckedItem: (ProductEntity) -> Unit,
    private val onUnCheckedItem: (ProductEntity) -> Unit,
    private val onAddQuantity: (ProductEntity) -> Unit,
    private val onMinQuantity: (ProductEntity) -> Unit,
    val context: Context
) : RecyclerView.Adapter<CartListAdapter.ViewHolder>() {
    private var listData = ArrayList<ProductEntity>()
    private lateinit var cartViewModel : CartViewModel

//    private lateinit var onItemClickCallback: OnItemClickCallback
//    private var onItemClick: CartListAdapter.OnAdapterListener? = null
//    fun setOnItemClick(onItemClick: CartListAdapter.OnAdapterListener){
//        this.onItemClick = onItemClick
//    }
//
//    fun setOnDeleteItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<ProductEntity>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyItemRemoved(newListData.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//        ViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
//        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
//        holder.binding.checkBoxSelectItem.isChecked = isCheckedAll

    }

    override fun getItemCount(): Int = listData.size

    inner class ViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductEntity) {
            cartViewModel = ViewModelProvider(context as FragmentActivity)[CartViewModel::class.java]
            binding.apply {
//                val isCheck = cartViewModel.isCheck(data.id)
//                Log.d("isCheckeddata", isCheck.toString())
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.image)
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .into(imgProduct)
//                tvTittleProduct.text = isCheck.toString()
                tvTittleProduct.text = data.name_product
                tvPriceProduct.text = formatRupiah(data.harga)
                tvQuantity.text = data.stockbuy.toString()
                if (data.is_checked == 1){
                    binding.checkBoxSelectItem.isChecked = true
                } else if (data.is_checked == 0){
                    binding.checkBoxSelectItem.isChecked = false
                }

                btnDelete.setOnClickListener {
                    onDeleteItem(data)
                }
//                btnDelete.setOnClickListener {
//                    onItemClickCallback.onItemClicked(data)
//                }
                checkBoxSelectItem.setOnCheckedChangeListener { _, p1 ->
                    if (p1) {
                        onCheckedItem.invoke(listData[adapterPosition])
                    } else if (!p1) {
                        onUnCheckedItem.invoke(listData[adapterPosition])
                    }
                }

                addQuantity.setOnClickListener {
                    onAddQuantity.invoke(listData[adapterPosition])
                }
                decreaseQuantity.setOnClickListener {
                    onMinQuantity.invoke(listData[adapterPosition])
                }
            }
        }
    }

//    interface OnItemClickCallback {
//        fun onItemClicked(data: ProductEntity)
//    }
//
//    interface OnAdapterListener {
//        fun onDelete(data: ProductEntity)
//        fun onIncrease(data: ProductEntity, position: Int)
//        fun onDecrease(data: ProductEntity, position: Int)
//        fun onChecked(data: ProductEntity, isChecked: Boolean)
//
//    }
//
//    fun removeData(id: Int) {
//        val index = listData.indexOfFirst { it.id == id }
//        if (index != -1) {
//            listData.removeAt(index)
//            notifyItemRemoved(index)
//        }
//    }
}