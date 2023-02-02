package com.wahyush04.androidphincon.ui.di

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.androidphincon.paging.ProductPagingSource
import com.wahyush04.core.data.product.DataListProductPaging

class ProductRepository(private val apiService: ApiService) {
    fun getProduct(search : String?): LiveData<PagingData<DataListProductPaging>> {
        Log.d("paging", "repositori")
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ProductPagingSource(search, apiService)
            }
        ).liveData
    }
}