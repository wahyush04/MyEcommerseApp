package com.wahyush04.androidphincon.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyush04.androidphincon.ui.cart.CartRepository
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductDatabase.Companion.getDatabase
import com.wahyush04.core.database.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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