package com.wahyush04.androidphincon.ui.main.home

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.androidphincon.core.repository.Repository
import com.wahyush04.androidphincon.ui.di.Injection
import com.wahyush04.androidphincon.paging.ECommerseRepository
import com.wahyush04.core.data.product.DataListProductPaging
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
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




