package com.wahyush04.androidphincon.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wahyush04.androidphincon.core.data.source.local.entity.NotificationEntity
import com.wahyush04.androidphincon.core.data.source.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class, NotificationEntity::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun notificationDao(): NotificationDao

}