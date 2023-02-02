package com.wahyush04.androidphincon.ui.main.home

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wahyush04.androidphincon.ui.di.Injection
import com.wahyush04.androidphincon.ui.di.ProductRepository
import com.wahyush04.core.data.product.DataListProductPaging
import com.wahyush04.core.helper.PreferenceHelper

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {

    fun productListPaging(search: String?): LiveData<PagingData<DataListProductPaging>> =
        productRepository.getProduct(search).cachedIn(viewModelScope)

}

class ViewModelFactory(private val context: Context, private val pref : PreferenceHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(Injection.provideRepository(context, pref)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
