package com.wahyush04.androidphincon.ui.cart

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductEntity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CartRepository(application: Application) {
    private var productDao: ProductDao?
    private var userDB: ProductDatabase? = ProductDatabase.getDatabase(application)
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        productDao = userDB?.favoriteDao()
    }

    fun getTrolley(): LiveData<List<ProductEntity>>?{
        return productDao?.getProduct()
    }

    fun countItems(): Int? {
        return productDao?.countItems()
    }

    fun getTotalHarga(): Int? {
        return productDao?.getTotalHargaChecked()
    }

    fun addTrolley(product: ProductEntity){
        executorService.execute{ productDao?.insert(product) }
    }

    fun deleteTrolley(data: ProductEntity){
        executorService.execute{ productDao?.delete(data) }
    }

    fun deleteTrolleyAdapter(data: ProductEntity){
        executorService.execute{ productDao?.delete(data) }
    }

    fun updateQuantity(quantity: Int, id: Int, newTotalHarga : Int): Int {
        return productDao!!.updateQuantity(quantity, id, newTotalHarga)
    }

    fun updateCheck(id: Int, state : Int): Int {
        Log.d("bisasamperepository", "bisa ni")
        return productDao!!.updateCheck(id, state)
    }

    fun isCheck(id: Int): Int {
        return productDao!!.isCheck(id)
    }


}