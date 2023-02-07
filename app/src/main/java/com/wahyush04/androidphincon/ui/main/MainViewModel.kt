package com.wahyush04.androidphincon.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wahyush04.androidphincon.ui.cart.CartRepository
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: ProductDao?
    private val cartRepository : CartRepository = CartRepository(application)
    private var db: ProductDatabase? = ProductDatabase.getDatabase(application)
    init {
        favDao = db?.favoriteDao()
    }

    fun countTrolley() = favDao?.countItems()

//    suspend fun totalTrolley() : Int? {
//        return cartRepository.countItems()
//    }

//    val countAllCart: Int? = cartRepository.countItems()

}