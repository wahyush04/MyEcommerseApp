package com.wahyush04.androidphincon.api

import android.content.Context
import android.util.Log
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiConfig {
    companion object{
        fun getApiService(
            pref : PreferenceHelper,
            context : Context): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(HeaderInterceptor(pref)) //header
                .addInterceptor(AuthBadResponse(pref, context)) // 401 bad response
                .authenticator(AuthAuthenticator(pref)) // get refresh token
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}