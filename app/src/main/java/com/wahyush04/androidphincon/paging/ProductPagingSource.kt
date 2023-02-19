package com.wahyush04.androidphincon.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.core.data.product.DataListProductPaging
import javax.inject.Inject

class ProductPagingSource(
    private val search: String?,
    private val apiService: ApiService
    ) : PagingSource<Int, DataListProductPaging>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataListProductPaging> {
        Log.d("paging", "loadPagingsource")
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getProductPaging(search, position)
            Log.d("paging", "loadPagingsource")
            LoadResult.Page(
                data = responseData.success!!.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.success!!.data.isEmpty()) null else position + 5
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DataListProductPaging>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}