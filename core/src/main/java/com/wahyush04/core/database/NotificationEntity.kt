package com.wahyush04.core.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "tbl_notification")
@Parcelize
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "message")
    val message: String?,
    @ColumnInfo(name = "date")
    val date: String?,
    @ColumnInfo(name = "status")
    val status: Int,
    @ColumnInfo(name = "isCheck")
    val isCheck: Int
) : Parcelable
