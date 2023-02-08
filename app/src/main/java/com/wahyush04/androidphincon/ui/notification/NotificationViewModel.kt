package com.wahyush04.androidphincon.ui.notification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.wahyush04.core.database.NotificationDao
import com.wahyush04.core.database.NotificationEntity
import com.wahyush04.core.database.ProductDatabase

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private var notifDao: NotificationDao
    private val notificationRepository : NotificationRepository = NotificationRepository(application)

    init {
        notifDao = ProductDatabase.getDatabase(application).NotificationDao()
    }

    fun getNotification(): LiveData<List<NotificationEntity>>?{
        return notificationRepository.getNotification()
    }

    fun insertNotif(data : NotificationEntity){
        notificationRepository.insertNotif(data)
    }

    fun deleteNotif(data: NotificationEntity){
        notificationRepository.deleteNotif(data)
    }

    fun updateStatus(id : Int, status : Int) {
        notificationRepository.updateStatus(id, status)
    }
}
