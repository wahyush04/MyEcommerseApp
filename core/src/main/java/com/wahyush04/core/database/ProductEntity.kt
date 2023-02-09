package com.wahyush04.core.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tbl_product")
@Parcelize
data class ProductEntity (
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id : Int,
        @ColumnInfo(name = "name_product")
        val name_product : String,
        @ColumnInfo(name = "harga")
        val harga : Int,
        @ColumnInfo(name = "total_harga")
        val total_harga : Int,
        @ColumnInfo(name = "stock")
        val stock : Int,
        @ColumnInfo(name = "stock_buy")
        val stockbuy : Int,
        @ColumnInfo(name = "image")
        val image : String,
        @ColumnInfo(name = "is_checked")
        val is_checked : Int
) : Parcelable


@Parcelize
data class DataTrolley(
        @ColumnInfo(name = "id")
        val id : Int,
        @ColumnInfo(name = "stock_buy")
        val stock_buy : Int,
) : Parcelable