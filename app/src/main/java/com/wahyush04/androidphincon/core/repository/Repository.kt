package com.wahyush04.androidphincon.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.core.data.source.local.LocalDataSource
import com.wahyush04.androidphincon.core.data.source.local.entity.DataTrolley
import com.wahyush04.androidphincon.core.data.source.local.entity.NotificationEntity
import com.wahyush04.androidphincon.core.data.source.local.entity.ProductEntity
import com.wahyush04.androidphincon.core.data.source.remote.RemoteDataSource
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.product.DataListProductPaging
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.data.register.RegisterResponse
import com.wahyush04.core.data.updaterating.UpdateRatingResponse
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.updatestock.UpdateStockResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): Flow<Resource<LoginResponse>> =
        remoteDataSource.login(email, password, tokenFcm)

    override fun register(
        image: MultipartBody.Part?,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ): Flow<Resource<RegisterResponse>> =
        remoteDataSource.register(image, email, password, name, phone, gender)

    override fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Resource<ProductResponse>> =
        remoteDataSource.getFavoriteProduct(userId, search)

    override fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ChangePasswordResponse>> =
        remoteDataSource.changePassword(id, password, newPassword, confirmPassword)

    override fun changeImage(
        id: String,
        image: MultipartBody.Part
    ): Flow<Resource<ChangeImageResponse>> =
        remoteDataSource.changeImage(id, image)

    override fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<DetailProductResponse>> =
        remoteDataSource.detailProduct(idProduk, idUser)

    override fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>> =
        remoteDataSource.addFavorite(idProduk, idUser)

    override fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>> =
        remoteDataSource.removeFavorite(idProduk, idUser)

    override fun getOtherProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>> =
        remoteDataSource.getOtherProduk(idUser)

    override fun getHistoryProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>> =
        remoteDataSource.getHistoryProduk(idUser)

    override fun buyProduct(
        requestBody: UpdateStockRequestBody
    ): Flow<Resource<UpdateStockResponse>> =
        remoteDataSource.buyProduct(requestBody)

    override fun updateRating(
        id: Int,
        rate: String
    ): Flow<Resource<UpdateRatingResponse>> =
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
        localDataSource.updateStatusCheckedNotif()

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