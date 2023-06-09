package com.wahyush04.androidphincon.ui.notification

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ListNotificationBinding
import com.wahyush04.core.data.source.local.entity.NotificationEntity
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class NotificationListAdapter(
    private val onItemClick: (NotificationEntity) -> Unit,
    private val onCheckedItem: (NotificationEntity) -> Unit,
    private val onUnCheckedItem: (NotificationEntity) -> Unit,
    val multiSelectState: Boolean,
    val context: Context
) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {
    private var listData = ArrayList<NotificationEntity>()
    private lateinit var notificationViewModel : NotificationViewModel

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newListData: List<NotificationEntity>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyItemRemoved(newListData.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)

    }
    override fun getItemCount(): Int = listData.size
    inner class ViewHolder(val binding: ListNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NotificationEntity) {
            notificationViewModel = ViewModelProvider(context as FragmentActivity)[NotificationViewModel::class.java]
            binding.apply {
                val timestamp = data.date?.toLong() // Example Unix timestamp in milliseconds
                val date = Date(timestamp!!)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                val dateStr = dateFormat.format(date)
                tvDate.text = dateStr.toString()
                tvTitleNotif.text = data.title
                tvTitleNotif.isSelected = true
                tvMessageNotif.text = data.message
                tvMessageNotif.isSelected = true
                if (data.status == 0){
                    cardListNotif.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue))
                }else if (data.status == 1){
                    cardListNotif.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
                }
                if (!multiSelectState){
                    cbItemNotif.visibility = View.GONE
                    cardListNotif.setOnClickListener {
                        onItemClick.invoke(listData[adapterPosition])
                    }
                }else{
                    cbItemNotif.visibility = View.VISIBLE
                }
                cbItemNotif.setOnCheckedChangeListener(null)
                cbItemNotif.setOnCheckedChangeListener { _, state ->
                    if (state){
                        onCheckedItem.invoke(listData[adapterPosition])
                    }else if (!state){
                        onUnCheckedItem.invoke(listData[adapterPosition])
                    }
                }
            }

        }
    }
}