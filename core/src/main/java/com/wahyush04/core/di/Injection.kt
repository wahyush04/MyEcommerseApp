package com.wahyush04.core.di

import android.content.Context
import com.wahyush04.core.api.ApiConfig
import com.wahyush04.core.data.PhinconRepository

object Injection {
    fun provideRepository(context: Context): PhinconRepository {
        val apiService = ApiConfig.getApiService()
        return PhinconRepository(apiService)
    }
}