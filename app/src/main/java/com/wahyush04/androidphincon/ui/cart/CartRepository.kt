package com.wahyush04.androidphincon.ui.cart

import android.app.Application
import androidx.lifecycle.LiveData
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CartRepository(application: Application) {
    private var favDao: ProductDao?
    private var userDB: ProductDatabase? = ProductDatabase.getDatabase(application)
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        favDao = userDB?.favoriteDao()
    }

    fun getTrolley(): LiveData<List<ProductEntity>>?{
        return favDao?.getProduct()
    }

    fun addTrolley(product: ProductEntity){
        executorService.execute{ favDao?.insert(product) }
    }

    fun deleteTrolley(data: ProductEntity){
        executorService.execute{ favDao?.delete(data) }
    }

    fun deleteTrolleyAdapter(data: ProductEntity){
        executorService.execute{ favDao?.delete(data) }
    }
}