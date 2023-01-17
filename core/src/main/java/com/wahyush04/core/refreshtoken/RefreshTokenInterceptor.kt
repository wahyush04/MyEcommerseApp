package com.wahyush04.core.refreshtoken

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wahyush04.core.Constant
import com.wahyush04.core.api.ApiConfig
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.refreshtoken.RefreshTokenResponse
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback

class RefreshTokenInterceptor(private val preferences: PreferenceHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the request and the response
        val request = chain.request()
        var response = chain.proceed(request)

        Log.d("responseCode", response.code.toString())

        // Check if the response is a 401 Unauthorized
        if (response.code == 401) {
            // Get the refresh token

                val refreshToken = preferences.getPreference(Constant.REFRESH_TOKEN)
                val token = preferences.getPreference(Constant.TOKEN)
                val idUSer = preferences.getPreference(Constant.ID)
                Log.d("tokenInterceptor", "$refreshToken")

            // Refresh the access token using the refresh token
            Log.d("authAccessToken", preferences.getPreference(Constant.TOKEN).toString())
            println("old accces token " + preferences.getPreference(Constant.TOKEN).toString())

            response.close()
            refreshAccessToken(idUSer!!.toInt(),token.toString(),refreshToken.toString(), preferences, chain)
//            response.close()
            // Save the new access token

//            preferences.edit().putString(Constant.TOKEN, getNewToken().value.toString()).apply()
//            preferences.edit().putString(Constant.REFRESH_TOKEN, getNewRefreshToken().value.toString()).apply()
//
//             Create a new request with the new access token
            val newAksesToken = preferences.getPreference(Constant.TOKEN)
            val newRefreshAksesToken = preferences.getPreference(Constant.REFRESH_TOKEN)

            runBlocking {
//                preferences.putNewToken(getNewToken().toString(), getNewRefreshToken().toString())
                Log.d("authAccessToken", newAksesToken.toString())
                println("new accces token : $newRefreshAksesToken")

                if (newAksesToken != null){
                    val newRequest = request.newBuilder()
                        .addHeader("apikey", Constant.APIKEY)
                        .addHeader("Authorization", newAksesToken)
                        .build()
                    response.close()
                    // Proceed with the new request
                    response = chain.proceed(newRequest)
                } else {
                    response.close()
                }

            }
        }

        return response
    }

//    val newAccessTokenRetro = MutableLiveData<String>()
//    val newRefreshAccessTokenRetro = MutableLiveData<String>()

    private fun refreshAccessToken(idUser : Int, token : String, refreshToken: String, preferences: PreferenceHelper, chain: Interceptor.Chain ) {
        // Make a request to the refresh token endpoint using the refresh token
        // Parse the response to get the new access token
        // Return the new access token
        // You can use Retrofit or OkHttpClient for this

        val client = ApiConfig.getApiService(preferences).refreshToken(idUser, token, refreshToken)
        client.enqueue(object : Callback<RefreshTokenResponse>{
            override fun onResponse(
                call: Call<RefreshTokenResponse>,
                response: retrofit2.Response<RefreshTokenResponse>
            ) {
                if (response.isSuccessful){
//                    newAccessTokenRetro.value = response.body()!!.success.access_token
//                    newRefreshAccessTokenRetro.value = response.body()!!.success.refresh_token
                    preferences.putNewToken(response.body()!!.success.access_token, response.body()!!.success.refresh_token)
                } else {
//                    val request = chain.request()
//                    var response = chain.proceed(request)
//                    response.close()
                }
                Log.d("newAccessToken", response.code().toString())
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                TODO("Not yet implemented")
//                val request = chain.request()
//                var response = chain.proceed(request)
//                response.close()
            }

        })


    }
//    fun getNewToken(): LiveData<String>{
//        return newAccessTokenRetro
//    }
//
//    fun getNewRefreshToken(): LiveData<String>{
//        return newRefreshAccessTokenRetro
//    }


}