package com.wahyush04.androidphincon.ui.notification

import android.app.Application
import androidx.lifecycle.LiveData
import com.wahyush04.core.database.NotificationDao
import com.wahyush04.core.database.NotificationEntity
import com.wahyush04.core.database.ProductDatabase

class NotificationRepository(application: Application) {
    private var notifDao: NotificationDao?
    private var userDB: ProductDatabase? = ProductDatabase.getDatabase(application)

    init {
        notifDao = userDB?.NotificationDao()
    }

    fun getNotification(): LiveData<List<NotificationEntity>>?{
        return notifDao?.getNotification()
    }

    fun insertNotif(data: NotificationEntity){
        notifDao?.insert(data)
    }

    fun deleteNotif(data: NotificationEntity){
        notifDao?.delete(data)
    }

    fun updateStatus(id : Int, status : Int): Int {
        return notifDao!!.updateStatus(id, status)
    }
}