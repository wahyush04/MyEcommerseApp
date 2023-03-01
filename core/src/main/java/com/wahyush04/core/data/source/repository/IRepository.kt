package com.wahyush04.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wahyush04.core.data.source.local.entity.DataTrolley
import com.wahyush04.core.data.source.local.entity.NotificationEntity
import com.wahyush04.core.data.source.local.entity.ProductEntity
import com.wahyush04.core.data.Result
import com.wahyush04.core.data.Resource
import com.wahyush04.core.data.source.remote.response.changeimage.ChangeImageResponse
import com.wahyush04.core.data.source.remote.response.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.source.remote.response.detailproduct.DetailProductResponse
import com.wahyush04.core.data.source.remote.response.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.source.remote.response.login.LoginResponse
import com.wahyush04.core.data.source.remote.response.product.DataListProductPaging
import com.wahyush04.core.data.source.remote.response.product.ProductResponse
import com.wahyush04.core.data.source.remote.response.register.RegisterResponse
import com.wahyush04.core.data.source.remote.response.updaterating.UpdateRatingResponse
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IRepository {

    //Remote Interface Repository

    fun getProduct(
        search: String?
    ): LiveData<PagingData<DataListProductPaging>>

    fun login(
        email: String,
        password: String,
        tokenFcm: String?
    ): Flow<Result<LoginResponse>>

    fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        gender: Int,
        image: MultipartBody.Part?
    ): Flow<Result<RegisterResponse>>

    fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Result<ProductResponse>>

    fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Result<ChangePasswordResponse>>

    fun changeImage(
        id: String,
        image: MultipartBody.Part
    ): Flow<Result<ChangeImageResponse>>

    fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<DetailProductResponse>>

    fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>>

    fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>>

    fun getOtherProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>>

    fun getHistoryProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>>

    fun buyProduct(
        requestBody: UpdateStockRequestBody
    ) : Flow<Result<UpdateStockResponse>>

    fun updateRating(
        id : Int,
        rate : String
    ) : Flow<Result<UpdateRatingResponse>>


    //Local Interface Cart Repository

    fun getTrolley(): LiveData<List<ProductEntity>>

    fun getCheckedTrolley(): List<DataTrolley>

    fun deleteCheckedTrolley(): Int

    fun insertTrolley(data: ProductEntity)

    fun countItemsTrolley(): Int

    fun countItemsCheckedTrolley(): Int

    fun getTotalHargaTrolley(): Int

    fun isTrolley(id : Int): Int

    fun addTrolley(product: ProductEntity)

    fun deleteTrolley(data: ProductEntity)

    fun updateQuantityTrolley(quantity: Int, id: Int, newTotalHarga : Int): Int

    fun checkALlTrolley(state : Int): Int

    fun updateChrolley(id: Int, state : Int): Int

    fun isCheckTrolley(id: Int): Int

    fun countTrolley() : Int

    fun updateSceck(id : Int, state : Int): Int

    //Local Interface Notification Repository

    fun getNotification(): LiveData<List<NotificationEntity>>

    fun countNotif() : Int
    fun countCheckedNotif() : Int

    fun deleteCheckedNotif()

    fun insertNotif(data: NotificationEntity)


    fun unCheckAllNotif()

    fun updateStatusCheckedNotif()

    fun updateStatusNotif(id : Int, status : Int): Int
    fun updateCheckNotif(id : Int, status : Int): Int
    fun readAllNotif(): Int
}