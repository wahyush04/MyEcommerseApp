package com.wahyush04.androidphincon.ui.paymentmethod

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.PaymentMethodBinding
import com.wahyush04.androidphincon.databinding.PaymentMethodListBinding
import com.wahyush04.core.data.remoteconfig.DataItem
import com.wahyush04.core.data.remoteconfig.PaymentMethod
import java.util.*

class PaymentMethodAdapter(
    private val onItemClick: (DataItem) -> Unit,
    dataItems: List<DataItem>,
    val context : Context
) : RecyclerView.Adapter<PaymentMethodAdapter.ListViewHolder>() {
    private var dataItems: List<DataItem> = ArrayList()
    init {
        this.dataItems = dataItems
    }
    private val listProduct = ArrayList<DataItem>()
    private var onItemClickCallback: OnItemClickCallback? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodAdapter.ListViewHolder {
        val view = PaymentMethodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    inner class ListViewHolder(val binding: PaymentMethodListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: DataItem){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(data)
            }
            when (data.id){
                "va_bca" ->
                    Glide.with(itemView)
                        .load(R.drawable.bca)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_mandiri" ->
                    Glide.with(itemView)
                        .load(R.drawable.mandiri)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_bri" ->
                    Glide.with(itemView)
                        .load(R.drawable.bri)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_bni" ->
                    Glide.with(itemView)
                        .load(R.drawable.bni)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_btn" ->
                    Glide.with(itemView)
                        .load(R.drawable.btn)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "va_danamon" ->
                    Glide.with(itemView)
                        .load(R.drawable.danamon)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_gopay" ->
                    Glide.with(itemView)
                        .load(R.drawable.gopay)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_ovo" ->
                    Glide.with(itemView)
                        .load(R.drawable.ovo)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
                "ewallet_dana" ->
                    Glide.with(itemView)
                        .load(R.drawable.dana)
                        .fitCenter()
                        .into(binding.ivPaymentMethod)
            }
            binding.line.isVisible = data.order != 0
            binding.tvPaymentMethod.text = data.name
            if(data.status == false){
                binding.cardListItem.alpha = 0.4f
                binding.cardListItem.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.light_grey
                    )
                )
                binding.cardListItem.setOnClickListener {
                        Toast.makeText(context, "Payment not Supported", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.cardListItem.alpha = 1.0f
                binding.cardListItem.setOnClickListener {
                    onItemClick.invoke(data)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PaymentMethodAdapter.ListViewHolder, position: Int) {
        holder.bind(dataItems[position])
    }

    override fun getItemCount(): Int = dataItems.size

    interface OnItemClickCallback{
        fun onItemClicked(data: DataItem)
    }

}

class PaymentMethodAdapterHeader(
    private val onItemClick: (DataItem) -> Unit,
    private val onHeaderClick: (PaymentMethod) -> Unit,
    val context: Context
) : RecyclerView.Adapter<PaymentMethodAdapterHeader.ListViewHolder>() {
    private val listItemHeader = ArrayList<PaymentMethod>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: List<PaymentMethod>){
        listItemHeader.clear()
        listItemHeader.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentMethodAdapterHeader.ListViewHolder {
        val view = PaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listItemHeader[position])
    }

    inner class ListViewHolder(val binding: PaymentMethodBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: PaymentMethod){
            Log.d("listHeader", "1")

            binding.lineDevider.isVisible = data.order != 0
            binding.apply {
                tvHeaderPaymentMethod.text = data.type.toString()
                val sort = data.data as List<DataItem>
                val paymentAdapter = PaymentMethodAdapter(
                    {
                        onItemClick.invoke(it)
                    },
                    sort.sortedBy { it.order },
                    context
                )
                rvPaymentMethodList.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL,false)
                rvPaymentMethodList.adapter = paymentAdapter
            }

            binding.sectionPaymentMethod.setOnClickListener{
                onHeaderClick.invoke(data)
                val isVisible = binding.rvPaymentMethodList.isVisible
                if (isVisible){
                    Glide.with(context)
                        .load(R.drawable.baseline_keyboard_arrow_down_24)
                        .fitCenter()
                        .into(binding.ivDropDownIndicator)
                }else{
                    Glide.with(context)
                        .load(R.drawable.baseline_keyboard_arrow_up_24)
                        .fitCenter()
                        .into(binding.ivDropDownIndicator)
                }
                binding.rvPaymentMethodList.isVisible = !isVisible
            }
        }
    }

    override fun getItemCount(): Int = listItemHeader.size

}