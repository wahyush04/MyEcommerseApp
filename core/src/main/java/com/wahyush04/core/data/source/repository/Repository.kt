package com.wahyush04.core.data.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wahyush04.core.data.source.local.LocalDataSource
import com.wahyush04.core.data.source.local.entity.DataTrolley
import com.wahyush04.core.data.source.local.entity.NotificationEntity
import com.wahyush04.core.data.source.local.entity.ProductEntity
import com.wahyush04.core.data.source.remote.RemoteDataSource
import com.wahyush04.core.data.Resource
import com.wahyush04.core.data.Result
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
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IRepository {

    //Remote Repository
    override fun getProduct(search : String?): LiveData<PagingData<DataListProductPaging>> =
        remoteDataSource.getProduct(search)


    override fun login(
        email: String,
        password: String,
        tokenFcm: String?
    ): Flow<Result<LoginResponse>> =
        remoteDataSource.login(email, password, tokenFcm)

    override fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        gender: Int,
        image: MultipartBody.Part?
    ): Flow<Result<RegisterResponse>> =
        remoteDataSource.register(
            name.toRequestBody(),
            email.toRequestBody(),
            password.toRequestBody(),
            phone.toRequestBody(),
            gender,
            image
        )

    override fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Result<ProductResponse>> =
        remoteDataSource.getFavoriteProduct(userId, search)

    override fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Result<ChangePasswordResponse>> =
        remoteDataSource.changePassword(id, password, newPassword, confirmPassword)

    override fun changeImage(
        id: String,
        image: MultipartBody.Part
    ): Flow<Result<ChangeImageResponse>> =
        remoteDataSource.changeImage(id.toRequestBody(), image)

    override fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<DetailProductResponse>> =
        remoteDataSource.detailProduct(idProduk, idUser)

    override fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>> =
        remoteDataSource.addFavorite(idProduk, idUser)

    override fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>> =
        remoteDataSource.removeFavorite(idProduk, idUser)

    override fun getOtherProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>> =
        remoteDataSource.getOtherProduk(idUser)

    override fun getHistoryProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>> =
        remoteDataSource.getHistoryProduk(idUser)

    override fun buyProduct(
        requestBody: UpdateStockRequestBody
    ): Flow<Result<UpdateStockResponse>> =
        remoteDataSource.buyProduct(requestBody)

    override fun updateRating(
        id: Int,
        rate: String
    ): Flow<Result<UpdateRatingResponse>> =
        remoteDataSource.updateRating(id, rate)


    //Local Repository
    override fun getTrolley(): LiveData<List<ProductEntity>> =
        localDataSource.getTrolley()

    override fun getCheckedTrolley(): List<DataTrolley> =
        localDataSource.getCheckedTrolley()

    override fun deleteCheckedTrolley(): Int =
        localDataSource.deleteCheckedTrolley()

    override fun insertTrolley(data: ProductEntity) {
        localDataSource.insertTrolley(data)
    }

    override fun countItemsTrolley(): Int =
        localDataSource.countItemsTrolley()

    override fun countItemsCheckedTrolley(): Int =
        localDataSource.countItemsCheckedTrolley()

    override fun getTotalHargaTrolley(): Int =
        localDataSource.getTotalHargaTrolley()

    override fun isTrolley(id: Int): Int =
        localDataSource.isTrolley(id)

    override fun addTrolley(product: ProductEntity) =
        localDataSource.addTrolley(product)

    override fun deleteTrolley(data: ProductEntity) =
        localDataSource.deleteTrolley(data)

    override fun updateQuantityTrolley(quantity: Int, id: Int, newTotalHarga: Int): Int =
        localDataSource.updateQuantityTrolley(quantity, id, newTotalHarga)

    override fun checkALlTrolley(state: Int): Int =
        localDataSource.checkAllTrolley(state)

    override fun updateChrolley(id: Int, state: Int): Int =
        localDataSource.updateCheckTrolley(id, state)

    override fun isCheckTrolley(id: Int): Int =
        localDataSource.isCheckTrolley(id)

    override fun countTrolley(): Int =
        localDataSource.countTrolley()

    override fun countNotif(): Int =
        localDataSource.countNotif()

    override fun countCheckedNotif(): Int =
        localDataSource.countCheckedNotif()


    override fun deleteCheckedNotif() =
        localDataSource.deleteCheckedNotif()

    override fun readAllNotif(): Int =
        localDataSource.readAllNotif()

    override fun insertNotif(data: NotificationEntity) =
        localDataSource.insertNotif(data)


    override fun unCheckAllNotif() =
        localDataSource.unCheckAll()

    override fun updateStatusCheckedNotif() =
        localDataSource.updateStatusCheckedNotif()

    override fun updateStatusNotif(id: Int, status: Int) =
        localDataSource.updateStatusNotif(id, status)

    override fun updateCheckNotif(id: Int, status: Int) =
        localDataSource.updateCheckNotif(id, status)

    override fun updateSceck(id: Int, state: Int): Int =
        localDataSource.updateCheckNotif(id, state)

    override fun getNotification(): LiveData<List<NotificationEntity>> =
        localDataSource.getNotification()
}