package com.wahyush04.androidphincon.ui.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.ActivityNotificationBinding
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    private lateinit var adapter : NotificationListAdapter
    private val notificationViewModel: NotificationViewModel by viewModels()
    private lateinit var preferences : PreferenceHelper
    private var toolbarState : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        preferences = PreferenceHelper(this)
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this@NotificationActivity, MainActivity::class.java))
        }

        setToolbar(toolbarState)
    }

    private fun initAdapter(state : Boolean){
        adapter = NotificationListAdapter(
            {
                val id = it.id
                notificationViewModel.updateStatus(id, 1)
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
            {
                notificationViewModel.updateCheck(it.id, 1)
            },
            {
                notificationViewModel.updateCheck(it.id, 0)
            },
            state,
            this
        )

        binding.rvNotification.adapter = adapter
        binding.rvNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
        binding.rvNotification.setHasFixedSize(true)
        notificationViewModel.getNotification().observe(this@NotificationActivity){
            if (it.isNotEmpty()){
                showEmpty(false)
                binding.ivChecckbox.isGone = !toolbarState
                adapter.setData(it)
            }else{
                binding.ivChecckbox.isGone = true
                showEmpty(true)
            }
        }
    }

    private fun setToolbar(state: Boolean){
        binding.apply {
        if (!state){
            ivChecckbox.visibility = View.VISIBLE
            ivReadAllNotif.visibility = View.GONE
            ivDelete.visibility = View.GONE
            ivChecckbox.setOnClickListener {
                setToolbar(true)
            }
            ivBack.setOnClickListener {
                startActivity(Intent(this@NotificationActivity, MainActivity::class.java))
                finish()
            }
            tvTitleAppbar.text = "Notification"
            initAdapter(state)
            toolbarState = !toolbarState
        }else{
            ivChecckbox.visibility = View.GONE
            ivReadAllNotif.visibility = View.VISIBLE
            ivDelete.visibility = View.VISIBLE
            ivBack.setOnClickListener {
                notificationViewModel.unCheckAll()
                setToolbar(false)
            }
            ivDelete.setOnClickListener {
                Toast.makeText(this@NotificationActivity, "Item Deleted", Toast.LENGTH_SHORT).show()
                notificationViewModel.deleteCheckedNotif()
                setToolbar(false)
            }
            ivReadAllNotif.setOnClickListener {
                Toast.makeText(this@NotificationActivity, "All Checked Notif readed", Toast.LENGTH_SHORT).show()
                notificationViewModel.updateStatusChecked()
                notificationViewModel.unCheckAll()
                setToolbar(false)
            }
            tvTitleAppbar.text = "Multi Select"
            initAdapter(state)
            toolbarState = !toolbarState
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