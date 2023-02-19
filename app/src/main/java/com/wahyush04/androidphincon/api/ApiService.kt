package com.wahyush04.androidphincon.api

import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
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
    suspend fun userLogin(
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("token_fcm") token_fcm : String?
    ): LoginResponse

    @Multipart
    @POST("training_android/public/api/ecommerce/registration")
    suspend fun userRegister(
        @Part("name") name : RequestBody,
        @Part("email") email : RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone") phone : RequestBody,
        @Part("gender") gender : Int,
        @Part image : MultipartBody.Part? = null
    ): RegisterResponse

    @GET("training_android/public/api/ecommerce/get_list_product_paging")
    suspend fun getProductPaging(
        @Query("search") search : String?,
        @Query("offset") offset : Int?
    ) : ProductResponsePaging

    @FormUrlEncoded
    @PUT("training_android/public/api/ecommerce/change-password/{id}")
    suspend fun userChangePassword(
        @Path("id") id : String,
        @Field("password") password : String,
        @Field("new_password") new_Password : String,
        @Field("confirm_password") confirm_password : String,
    ): ChangePasswordResponse

    //change Image
    @Multipart
    @POST("training_android/public/api/ecommerce/change-image")
    suspend fun changeImage(
        @Part("id") id : RequestBody,
        @Part image : MultipartBody.Part
    ) : ChangeImageResponse

    //Refresh Token
    @FormUrlEncoded
    @Headers(*["apikey:TuIBt77u7tZHi8n7WqUC"])
    @POST("training_android/public/api/ecommerce/refresh-token")
    suspend fun refreshToken(
        @Field("id_user") id_user : Int?,
        @Field("access_token") access_token : String?,
        @Field("refresh_token") refresh_token : String?
    ) : Response<RefreshTokenResponse>

    @GET("training_android/public/api/ecommerce/get_detail_product")
    suspend fun getDetailProduct(
        @Query("id_product") id_product : Int,
        @Query("id_user") id_user : Int
    ) : DetailProductResponse


    @GET("training_android/public/api/ecommerce/get_list_product_favorite")
    suspend fun getFavorite(
        @Query("search") search : String?,
        @Query("id_user") id_user : Int
    ) : ProductResponse

    @GET("training_android/public/api/ecommerce/get_list_product_favorite")
    suspend fun getFavoriteProduct(
        @Query("search") search : String?,
        @Query("id_user") id_user : Int
    ) : ProductResponse

    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/add_favorite")
    suspend fun addFavorite(
        @Field("id_product")
        id_product : Int,
        @Field("id_user")
        id_user : Int,
    ) : AddRemoveFavResponse

    @FormUrlEncoded
    @POST("training_android/public/api/ecommerce/remove_favorite")
    suspend fun removeFavorite(
        @Field("id_product")
        id_product : Int,
        @Field("id_user")
        id_user : Int,
    ) : AddRemoveFavResponse

    @GET("training_android/public/api/ecommerce/get_list_product_other")
    suspend fun getOtherProducts(
        @Query("id_user")
        iduser: Int?
    ) : ProductResponse

    @GET("training_android/public/api/ecommerce/get_list_product_riwayat")
    suspend fun getProductSearchHistory(
        @Query("id_user")
        iduser: Int?
    ) : ProductResponse






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



}