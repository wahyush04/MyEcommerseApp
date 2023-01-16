package com.wahyush04.core.api

import androidx.annotation.Nullable
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

////    @Multipart
//    @FormUrlEncoded
//    @POST("training_android/public/api/ecommerce/registration")
//    fun userRegister(
//        @Header("apikey") apikey : String,
//        @Field("name") name : String,
//        @Field("email") email : String,
//        @Field("password") password: String,
//        @Field("phone") phone : String,
//        @Field("gender") gender : Int
//    ): Call<RegisterResponse>

    @Multipart
    @POST("training_android/public/api/ecommerce/registration")
    fun userRegister(
        @Header("apikey") apikey : String,
        @Part("name") name : RequestBody,
        @Part("email") email : RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone : RequestBody,
        @Part("gender") gender : Int,
        @Part image : MultipartBody.Part? = null
    ): Call<RegisterResponse>


    @FormUrlEncoded
    @PUT("training_android/public/api/ecommerce/change-password/{id}")
    fun userChangePassword(
        @Header("apikey") apikey : String,
        @Header("Authorization") Authorization : String,
        @Path("id") id : String,
        @Field("password") password : String,
        @Field("new_password") new_Password : String,
        @Field("confirm_password") confirm_password : String,
    ): Call<ChangePasswordResponse>
}