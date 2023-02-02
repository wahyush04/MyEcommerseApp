package com.wahyush04.androidphincon.api

import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.FavoriteResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.data.product.ProductResponsePaging
import com.wahyush04.core.data.refreshtoken.RefreshTokenResponse
import com.wahyush04.core.data.register.RegisterResponse
import com.wahyush04.core.data.updaterating.UpdateRatingResponse
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.updatestock.UpdateStockResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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


    @GET("training_android/public/api/ecommerce/get_list_product")
    fun getProduct(
        @Query("search") search : String?
    ) : Call<ProductResponse>


    @GET("training_android/public/api/ecommerce/get_list_product_favorite")
    fun getFavorite(
        @Query("search") search : String?,
        @Query("id_user") id_user : Int
    ) : Call<ProductResponse>

    @GET("training_android/public/api/ecommerce/get_detail_product")
    fun getDetailProduct(
        @Query("id_product") id_product : Int,
        @Query("id_user") id_user : Int
    ) : Call<DetailProductResponse>

    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/add_favorite")
    fun addFavorite(
        @Field("id_product") id_product : Int,
        @Field("id_user") id_user : Int,
    ) : Call<FavoriteResponse>

    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/remove_favorite")
    fun removeFavorite(
        @Field("id_product") id_product : Int,
        @Field("id_user") id_user : Int,
    ) : Call<FavoriteResponse>

    @POST("training_android/public/api/ecommerce/update-stock")
    fun buyProduct(
        @Body requestBody : UpdateStockRequestBody
    ) : Call <UpdateStockResponse>

    @FormUrlEncoded
    @PUT("training_android/public/api/ecommerce/update_rate/{id}")
    fun updateRating(
        @Path("id") id : Int,
        @Field("rate") rate : String
    ) : Call <UpdateRatingResponse>

    @GET("training_android/public/api/ecommerce/get_list_product_paging")
     suspend fun getProductPaging(
        @Query("search") search : String?,
        @Query("offset") offset : Int?
    ) : ProductResponsePaging
}