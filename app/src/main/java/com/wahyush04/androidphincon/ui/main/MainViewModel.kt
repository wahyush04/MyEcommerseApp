package com.wahyush04.androidphincon.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wahyush04.androidphincon.ui.cart.CartRepository
import com.wahyush04.core.database.NotificationDao
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: ProductDao?
    private var notifDao: NotificationDao?
    private val cartRepository : CartRepository = CartRepository(application)
    private var db: ProductDatabase? = ProductDatabase.getDatabase(application)
    init {
        favDao = db?.productDao()
        notifDao = db?.NotificationDao()
    }

    fun countTrolley() = favDao?.countItems()

    fun countNotif() = notifDao?.countItems()

    fun deleteCheckedNotif(): Int?{
        return notifDao?.deleteCheckedNotif()
    }

    fun readAllNotif(state :Int): Int?{
        return notifDao?.readAll(state)
    }

    fun updateSceck(id : Int, state : Int): Int?{
        return notifDao?.updateStatus(id, state)
    }



//    suspend fun totalTrolley() : Int? {
//        return cartRepository.countItems()
//    }

//    val countAllCart: Int? = cartRepository.countItems()

}