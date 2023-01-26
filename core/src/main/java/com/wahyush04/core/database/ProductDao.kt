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


//
//    @Query("SELECT count(*) FROM tbl_favorite WHERE tbl_favorite.id = :id")
//    suspend fun checkUser(id: Int) : Int

}