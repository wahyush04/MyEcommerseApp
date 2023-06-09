package com.wahyush04.core.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wahyush04.core.BaseFirebaseAnalytics
import com.wahyush04.core.api.ApiService
import com.wahyush04.core.data.source.remote.response.product.DataListProductPaging

class ProductPagingSource(
    private val search: String?,
    private val apiService: ApiService
    ) : PagingSource<Int, DataListProductPaging>() {

    private val firebaseAnalytics = BaseFirebaseAnalytics()

    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataListProductPaging> {
        return try {
            val offset = params.key ?: INITIAL_PAGE_INDEX
            //GA Slide 9 onPagingScroll
            firebaseAnalytics.onPagingScroll("Home", offset)
            val responseData = apiService.getProductPaging(search, offset)
            LoadResult.Page(
                data = responseData.success!!.data,
                prevKey = if (offset == INITIAL_PAGE_INDEX) null else offset - 1,
                nextKey = if (responseData.success.data.isEmpty()) null else offset + 5
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