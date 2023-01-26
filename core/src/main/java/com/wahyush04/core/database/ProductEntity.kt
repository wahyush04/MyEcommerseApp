package com.wahyush04.core.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tbl_favorite")
@Parcelize
data class ProductEntity (
        @PrimaryKey
        @ColumnInfo(name = "id")
        val id : Int,
        @ColumnInfo(name = "name_product")
        val name_product : String,
        @ColumnInfo(name = "harga")
        val harga : Int,
) : Parcelable