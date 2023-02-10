package com.wahyush04.androidphincon.ui.di

import android.content.Context
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.paging.ECommerseRepository
import com.wahyush04.core.helper.PreferenceHelper

object Injection {
    fun provideRepository(context: Context, pref : PreferenceHelper): ECommerseRepository {
//        val database = ProductDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(pref, context)
        return ECommerseRepository(apiService)
    }

}