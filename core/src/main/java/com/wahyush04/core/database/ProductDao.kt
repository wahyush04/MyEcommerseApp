package com.wahyush04.core.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: ProductEntity)

    @Delete
    fun delete(user: ProductEntity)

    @Query("SELECT * FROM tbl_product")
    fun getProduct(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM tbl_product")
    fun getTrolley(): LiveData<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM tbl_product")
    fun countItems(): Int

    @Query("UPDATE tbl_product SET stock_buy = :quantity, total_harga = :newTotalHarga WHERE id = :id")
    fun updateQuantity(quantity: Int, id: Int, newTotalHarga : Int): Int

    @Query("SELECT is_checked FROM tbl_product WHERE id = :id")
    fun isCheck(id: Int): Int
    @Query("UPDATE tbl_product SET is_checked = :state WHERE id = :id")
    fun updateCheck(id: Int, state : Int): Int

    @Query("SELECT SUM(total_harga) FROM tbl_product WHERE is_checked = 1")
    fun getTotalHargaChecked() : Int

    @Query("SELECT id,stock_buy FROM tbl_product WHERE is_checked = 1")
    fun getDataTrolley() : LiveData<List<DataTrolley>>


    
}