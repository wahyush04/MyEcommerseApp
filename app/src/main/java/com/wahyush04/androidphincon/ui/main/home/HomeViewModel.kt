package com.wahyush04.androidphincon.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.core.data.product.DataListProductPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IRepository
    ) : ViewModel() {
    fun productListPaging(
        search: String?
    ): LiveData<PagingData<DataListProductPaging>> =
        repository.getProduct(search).cachedIn(viewModelScope)
}




