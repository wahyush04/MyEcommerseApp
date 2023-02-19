package com.wahyush04.androidphincon.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.androidphincon.core.data.source.Result
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.data.product.DataListProductPaging

class ECommerseRepository(private val apiService: ApiService) {
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
    fun getProductFavoriteList(search : String, id : Int): LiveData<Result<List<DataListProduct>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFavoriteProduct(search, id)
            val articles = response.success.data
            val newsList = articles.map { data ->
                DataListProduct(
                    data.id,
                    data.name_product,
                    data.harga,
                    data.rate,
                    data.image,
                    data.date,
                    data.stock,
                    data.size,
                    data.weight,
                    data.type,
                    data.desc
                )
            }
            emit(Result.Success(newsList))
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: ECommerseRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): ECommerseRepository =
            instance ?: synchronized(this) {
                instance ?: ECommerseRepository(apiService)
            }.also { instance = it }
    }
}