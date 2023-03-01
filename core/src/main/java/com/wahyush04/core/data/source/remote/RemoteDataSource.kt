package com.wahyush04.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wahyush04.core.api.ApiService
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
import com.wahyush04.core.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    fun getProduct(search: String?): LiveData<PagingData<DataListProductPaging>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 5,
                pageSize = 5,
                prefetchDistance = 1
            ),
            pagingSourceFactory = {
                ProductPagingSource(search, apiService)
            }
        ).liveData
    }

    fun login(
        email: String,
        password: String,
        tokenFcm: String?
    ): Flow<Result<LoginResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.userLogin(email, password, tokenFcm)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }


    fun register(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phone: RequestBody,
        gender: Int,
        image: MultipartBody.Part?
    ): Flow<Result<RegisterResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.userRegister(name, email, password, phone, gender, image)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Result<ProductResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getFavorite(search, userId)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Result<ChangePasswordResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response =
                    apiService.userChangePassword(
                        id.toString(),
                        password,
                        newPassword,
                        confirmPassword
                    )
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun changeImage(
        id: RequestBody,
        image: MultipartBody.Part
    ): Flow<Result<ChangeImageResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response =
                    apiService.changeImage(
                        id,
                        image
                    )
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<DetailProductResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getDetailProduct(
                    idProduk,
                    idUser
                )
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.addFavorite(idProduk, idUser)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Result<AddRemoveFavResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.removeFavorite(idProduk, idUser)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun getOtherProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getOtherProducts(idUser)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun getHistoryProduk(
        idUser: Int
    ): Flow<Result<ProductResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.getProductSearchHistory(idUser)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun buyProduct(
        requestBody: UpdateStockRequestBody
    ): Flow<Result<UpdateStockResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.buyProduct(requestBody)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

    fun updateRating(
        id: Int,
        rate: String
    ): Flow<Result<UpdateRatingResponse>> {
        return flow {
            emit(Result.Loading)
            try {
                val response = apiService.updateRating(id, rate)
                emit(Result.Success(response))
            } catch (t: HttpException) {
                when (t.code()) {
                    401 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    429 -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Result.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }
}