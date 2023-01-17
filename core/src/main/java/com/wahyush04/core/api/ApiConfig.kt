package com.wahyush04.core.api

import android.content.Context
import android.content.SharedPreferences
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.refreshtoken.RefreshTokenInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(pref : PreferenceHelper): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor { chain: Interceptor.Chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("apikey", Constant.APIKEY)
                        .addHeader("Authorization", pref.getPreference(Constant.TOKEN).toString())
                        .build()

                    // Proceed with the new request
                    chain.proceed(newRequest)
                }
                .addInterceptor(loggingInterceptor)
                .addInterceptor(RefreshTokenInterceptor(pref))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://172.17.20.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}