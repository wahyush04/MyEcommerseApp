package com.wahyush04.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class, NotificationEntity::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun NotificationDao(): NotificationDao
    companion object{

        @Volatile
        var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: getDatabaseOnFragment(context).also { INSTANCE = it }
            }

        @JvmStatic
        fun getDatabase(context: Context): ProductDatabase{
            if (INSTANCE==null){
                synchronized(ProductDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "db_ecommerce")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as ProductDatabase
        }
        private fun getDatabaseOnFragment(appContext: Context): ProductDatabase{
            if (INSTANCE==null){
                synchronized(ProductDatabase::class){
                    INSTANCE = Room.databaseBuilder(appContext, ProductDatabase::class.java, "db_product")
                        .build()
                }
            }
            return INSTANCE as ProductDatabase
        }
    }
}