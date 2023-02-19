package com.wahyush04.androidphincon.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.product.DataListProductPaging
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.data.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IRepository {

    fun getProduct(
        search: String?
    ): LiveData<PagingData<DataListProductPaging>>

    fun login(
        email: String,
        password: String,
        tokenFcm: String?
    ): Flow<Resource<LoginResponse>>

    fun register(
        image: MultipartBody.Part?,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ): Flow<Resource<RegisterResponse>>

    fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Resource<ProductResponse>>

    fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ChangePasswordResponse>>

    fun changeImage(
        id: String,
        image: MultipartBody.Part
    ): Flow<Resource<ChangeImageResponse>>

    fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<DetailProductResponse>>

    fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>>

    fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>>

    fun getOtherProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>>

    fun getHistoryProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>>
}