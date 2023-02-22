package com.wahyush04.androidphincon.ui.main.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {
    fun getFavProduct(
        userId: Int,
        search: String? = null
    ) = repository.getFavoriteProduct(userId, search).asLiveData()

}