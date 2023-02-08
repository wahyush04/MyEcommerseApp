package com.wahyush04.androidphincon.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.ActivityNotificationBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.helper.PreferenceHelper

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter : NotificationListAdapter
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var preferences : PreferenceHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        preferences = PreferenceHelper(this)
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        initAdapter()

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this@NotificationActivity, MainActivity::class.java))
        }
    }

    private fun initAdapter(){
        adapter = NotificationListAdapter(
            {
                val id = it.id
                notificationViewModel.updateStatus(id, 1)
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)
                builder.setTitle(it.title)
                builder.setMessage(it.message)
                builder.setPositiveButton("OK") { dialog, which ->
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    // Perform some action when Cancel is clicked
                }
                val dialog = builder.create()
                dialog.show()
            },
            this
        )

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
        binding.rvNotification.setHasFixedSize(true)
        notificationViewModel.getNotification()?.observe(this@NotificationActivity){
            if (it.isNotEmpty()){
                showEmpty(false)
                adapter.setData(it)
            }else{
                showEmpty(true)
            }
        }
    }

    private fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
            binding.rvNotification.visibility = View.GONE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
            binding.rvNotification.visibility = View.VISIBLE
        }
    }
}