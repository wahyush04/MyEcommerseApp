package com.wahyush04.core.api

import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.register.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/authentication")
    fun userLogin(
        @Header("apikey") apikey : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<LoginResponse>

//    @Multipart
    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/registration")
    fun userRegister(
        @Header("apikey") apikey : String,
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password: String,
        @Field("phone") phone : String,
        @Field("gender") gender : Int
    ): Call<RegisterResponse>
}