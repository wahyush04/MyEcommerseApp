package com.wahyush04.androidphincon.api

import android.content.Context
import android.content.Intent
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthBadResponse(
    private val preferences: PreferenceHelper,
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            runBlocking {
                preferences.clear()
            }
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            return response
        }
        return response
    }
}
