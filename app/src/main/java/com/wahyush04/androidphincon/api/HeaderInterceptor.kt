package com.wahyush04.androidphincon.api

import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val preferences : PreferenceHelper
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            preferences.getPreference(Constant.TOKEN)
        }
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .header("apikey", "TuIBt77u7tZHi8n7WqUC")
            .header("Authorization", token.toString())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
