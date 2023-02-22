package com.wahyush04.androidphincon.core.data.source.local

import androidx.lifecycle.LiveData
import com.wahyush04.androidphincon.core.data.source.local.entity.DataTrolley
import com.wahyush04.androidphincon.core.data.source.local.entity.NotificationEntity
import com.wahyush04.androidphincon.core.data.source.local.entity.ProductEntity
import com.wahyush04.androidphincon.core.data.source.local.room.NotificationDao
import com.wahyush04.androidphincon.core.data.source.local.room.ProductDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cartDao : ProductDao,
    private val notificationDao : NotificationDao
){

    //Trolley Data Source
    fun getTrolley(): LiveData<List<ProductEntity>> {
        return cartDao.getProduct()
    }

    fun insertTrolley(data : ProductEntity) =
        cartDao.insert(data)

    fun getCheckedTrolley(): List<DataTrolley> {
        return cartDao.getTrolleyChecked()
    }

    fun deleteCheckedTrolley(): Int {
        return cartDao.deleteTrolley()
    }

    fun countItemsTrolley(): Int {
        return cartDao.countItems()
    }

    fun countItemsCheckedTrolley(): Int {
        return cartDao.countItemsCheck()
    }

    fun getTotalHargaTrolley(): Int {
        return cartDao.getTotalHargaChecked()
    }

    fun isTrolley(id : Int): Int {
        return cartDao.isTrolley(id)
    }

    fun addTrolley(product: ProductEntity){
        cartDao.insert(product)
    }

    fun deleteTrolley(data: ProductEntity){
        cartDao.delete(data)
    }

    fun updateQuantityTrolley(quantity: Int, id: Int, newTotalHarga : Int): Int {
        return cartDao.updateQuantity(quantity, id, newTotalHarga)
    }

    fun checkAllTrolley(state : Int): Int {
        return cartDao.checkAll(state)
    }

    fun updateCheckTrolley(id: Int, state : Int): Int {
        return cartDao.updateCheck(id, state)
    }

    fun isCheckTrolley(id: Int): Int {
        return cartDao.isCheck(id)
    }

    fun countTrolley() = cartDao.countItems()

    //Notification Data Source
    fun getNotification(): LiveData<List<NotificationEntity>> =
        notificationDao.getNotification()

    fun countNotif() = notificationDao.countItems()

    fun deleteCheckedNotif() {
        return notificationDao.deleteCheckedNotif()
    }

    fun insertNotif(data : NotificationEntity) {
        return notificationDao.insert(data)
    }

    fun readAllNotif(): Int {
        return notificationDao.readAll()
    }

    fun updateCheckNotif(id : Int, state : Int): Int {
        return notificationDao.updateCheck(id, state)
    }

    fun updateStatusNotif(id : Int, status : Int): Int =
        notificationDao.updateStatus(id, status)

    fun updateStatusCheckedNotif() =
        notificationDao.updateStatusChecked()
}