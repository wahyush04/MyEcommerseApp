//package com.wahyush04.androidphincon.refreshtoken
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.wahyush04.androidphincon.api.ApiConfig
//import com.wahyush04.core.Constant
//import com.wahyush04.core.helper.PreferenceHelper
//import okhttp3.Interceptor
//import okhttp3.Response
//
//class RefreshTokenInterceptor(private val preferences: PreferenceHelper, contex : Context) : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        // Get the request and the response
//        val request = chain.request()
//        var response = chain.proceed(request)
//
//        // Check if the response is a 401 Unauthorized
//        if (response.code == 401) {
//            // Get the refresh token
//            val id = preferences.getPreference(Constant.TOKEN)
//            val token = preferences.getPreference(Constant.TOKEN)
//            val refreshToken = preferences.getPreference(Constant.TOKEN)
//
//            // Refresh the access token using the refresh token
//            val newAccessToken = refreshAccessToken(id.toString(), token.toString(), refreshToken.toString())
//
//            // Save the new access token
//            preferences.putNewToken()
//
//            // Create a new request with the new access token
//            val newRequest = request.newBuilder()
//                .addHeader("apikey", Constant.APIKEY)
//                .build()
//
//            // Proceed with the new request
//            response = chain.proceed(newRequest)
//        }
//
//        return response
//    }
//
//    private fun refreshAccessToken(id: String, token: String, refreshToken : String, preferences: PreferenceHelper, contex : Context) {
//        // Make a request to the refresh token endpoint using the refresh token
//        // Parse the response to get the new access token
//        // Return the new access token
//        // You can use Retrofit or OkHttpClient for this
//        val client = ApiConfig.getApiService(pref, context).userChangePassword(id, password, new_password, confirm_password)
//    }
//}