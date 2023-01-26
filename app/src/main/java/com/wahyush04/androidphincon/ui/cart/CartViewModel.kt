package com.wahyush04.androidphincon.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: ProductDao
    private val cartRepository : CartRepository = CartRepository(application)
    init {
        favDao = ProductDatabase.getInstance(application).favoriteDao()
    }

    fun getTrolley(): LiveData<List<ProductEntity>> {
        return favDao.getProduct()
    }

    fun insert(data: ProductEntity){
        cartRepository.addTrolley(data)
    }

    fun delete(data : ProductEntity){
        cartRepository.deleteTrolley(data)
    }

    fun deleteCart(data : ProductEntity) = viewModelScope.launch(Dispatchers.IO) { cartRepository.deleteTrolley(data) }
}