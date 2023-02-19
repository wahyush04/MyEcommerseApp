package com.wahyush04.androidphincon.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.paging.ProductPagingSource
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.data.product.DataListProductPaging
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.data.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
     fun getProduct(search : String?): LiveData<PagingData<DataListProductPaging>> {
        Log.d("paging", "repositori")
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
    ): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.userLogin(email, password, tokenFcm)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }


     fun register(
        image: MultipartBody.Part?,
        email: RequestBody,
        password: RequestBody,
        name: RequestBody,
        phone: RequestBody,
        gender: Int
    ): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.userRegister(name, email, password, phone, gender, image)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun getFavoriteProduct(
        userId: Int,
        search: String?
    ): Flow<Resource<ProductResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.getFavorite(search, userId)
                    val data = response.success.data
                    if (data.isNotEmpty()) {
                        emit(Resource.Success(response))
                    } else {
                        emit(Resource.Empty())
                    }
                } catch (t: HttpException) {
                    when (t.code()) {
                        400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun changePassword(
        id: Int,
        password: String,
        newPassword: String,
        confirmPassword: String
    ): Flow<Resource<ChangePasswordResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response =
                        apiService.userChangePassword(id.toString(), password, newPassword, confirmPassword)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun changeImage(
        id: String,
        image: MultipartBody.Part
    ): Flow<Resource<ChangeImageResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.changeImage(id.toRequestBody("text/plain".toMediaType()), image)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<DetailProductResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.getDetailProduct(idProduk, idUser)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                        429 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.addFavorite(idProduk, idUser)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                        429 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): Flow<Resource<AddRemoveFavResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.removeFavorite(idProduk, idUser)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                        429 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun getOtherProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.getOtherProducts(idUser)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                        429 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }

     fun getHistoryProduk(
        idUser: Int
    ): Flow<Resource<ProductResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                try {
                    val response = apiService.getOtherProducts(idUser)
                    emit(Resource.Success(response))
                } catch (t: HttpException) {
                    when (t.code()) {
                        401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                        429 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    }
                }
            } catch (t: HttpException) {
                when (t.code()) {
                    400 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    401 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    404 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    500 -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                    else -> emit(Resource.Error(true, t.code(), t.response()?.errorBody()))
                }
            }
        }
    }
}