package com.wahyush04.androidphincon.ui.cart

import android.app.Application
import androidx.lifecycle.LiveData
import com.wahyush04.core.database.DataTrolley
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductEntity

class CartRepository(application: Application) {
    private var productDao: ProductDao?
    private var userDB: ProductDatabase? = ProductDatabase.getDatabase(application)

    init {
        productDao = userDB?.productDao()
    }

    fun getTrolley(): LiveData<List<ProductEntity>>?{
        return productDao?.getProduct()
    }

    fun getCheckedTrolley(): List<DataTrolley>?{
        return productDao?.getTrolleyChecked()
    }

    fun deleteCheckedTrolley(): Int?{
        return productDao?.deleteTrolley()
    }

    fun countItems(): Int? {
        return productDao?.countItems()
    }

    fun countItemsChecked(): Int? {
        return productDao?.countItemsCheck()
    }

    fun getTotalHarga(): Int? {
        return productDao?.getTotalHargaChecked()
    }

    fun isTrolley(id : Int): Int? {
        return productDao?.isTrolley(id)
    }

    fun addTrolley(product: ProductEntity){
        productDao?.insert(product)
    }

    fun deleteTrolley(data: ProductEntity){
        productDao?.delete(data)
    }

    fun updateQuantity(quantity: Int, id: Int, newTotalHarga : Int): Int {
        return productDao!!.updateQuantity(quantity, id, newTotalHarga)
    }

    fun checkALl(state : Int): Int {
        return productDao!!.checkAll(state)
    }

    fun updateCheck(id: Int, state : Int): Int {
        return productDao!!.updateCheck(id, state)
    }

    fun isCheck(id: Int): Int {
        return productDao!!.isCheck(id)
    }
}