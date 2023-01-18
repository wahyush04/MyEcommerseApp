package com.wahyush04.androidphincon.api

import com.wahyush04.core.Constant
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.refreshtoken.RefreshTokenResponse
import com.wahyush04.core.data.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/authentication")
    fun userLogin(
        @Header("apikey") apikey : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Call<LoginResponse>

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

//    @Headers(*["apikey:TuIBt77u7tZHi8n7WqUC"])
    @FormUrlEncoded
    @PUT("training_android/public/api/ecommerce/change-password/{id}")
    fun userChangePassword(
        @Path("id") id : String,
        @Field("password") password : String,
        @Field("new_password") new_Password : String,
        @Field("confirm_password") confirm_password : String,
    ): Call<ChangePasswordResponse>

    //change Image
    @Multipart
    @POST("training_android/public/api/ecommerce/change-image")
    fun changeImage(
        @Part("id") id : RequestBody,
        @Part image : MultipartBody.Part
    ) : Call<ChangeImageResponse>

    //Refresh Token
    @FormUrlEncoded
    @Headers(*["apikey:TuIBt77u7tZHi8n7WqUC"])
    @POST("training_android/public/api/ecommerce/refresh-token")
    suspend fun refreshToken(
        @Field("id_user") id_user : Int?,
        @Field("access_token") access_token : String?,
        @Field("refresh_token") refresh_token : String?
    ) : Response<RefreshTokenResponse>
}