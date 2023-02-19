package com.wahyush04.androidphincon.core.data.source.local

import com.wahyush04.core.database.NotificationDao
import com.wahyush04.core.database.ProductDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourcee @Inject constructor(
    private val cartDao : ProductDao,
    private val notificationDao : NotificationDao
){

}