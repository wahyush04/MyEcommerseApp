package com.wahyush04.core.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProductEntity::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun favoriteDao(): ProductDao
    companion object{

        @Volatile
        var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: getDatabaseOnFragment(context).also { INSTANCE = it }
            }

        fun getDatabase(context: Application): ProductDatabase{
            if (INSTANCE==null){
                synchronized(ProductDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ProductDatabase::class.java, "db_product")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as ProductDatabase
        }
        fun getDatabaseOnFragment(appContext: Context): ProductDatabase{
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