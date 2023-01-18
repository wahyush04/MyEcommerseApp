package com.wahyush04.androidphincon.api

import android.util.Log
import com.squareup.moshi.Moshi
import com.wahyush04.core.Constant
import com.wahyush04.core.data.refreshtoken.RefreshTokenResponse
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class AuthAuthenticator(
    private val preferences: PreferenceHelper
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        return runBlocking {
            val accessToken = preferences.getPreference(Constant.TOKEN)
            val refreshToken = preferences.getPreference(Constant.REFRESH_TOKEN)
            val userId = preferences.getPreference(Constant.ID)

            val newToken = getNewToken(accessToken, refreshToken, userId?.toInt())
            println("token $newToken")

            if (!newToken.isSuccessful || newToken.body() == null || newToken.code() == 401) {
                preferences.clearToken()
            }

            newToken.body()?.let {
                val newUserToken = it.success.access_token
                val newRefreshToken = it.success.refresh_token
                preferences.putNewToken(newUserToken, newRefreshToken)
                Log.d("newToken", it.success.access_token)
                Log.d("newRefreshToken", it.success.refresh_token)
                response.request.newBuilder()
                    .header("Authorization", it.success.access_token)
                    .build()
            }
        }
    }

    private suspend fun getNewToken(
        accessToken: String?,
        refreshToken: String?,
        userId: Int?
    ): retrofit2.Response<RefreshTokenResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(ApiService::class.java)
        return service.refreshToken(userId, accessToken, refreshToken )
    }
}
