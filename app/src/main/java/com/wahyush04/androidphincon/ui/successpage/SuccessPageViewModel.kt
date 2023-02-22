package com.wahyush04.androidphincon.ui.successpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.core.data.updaterating.UpdateRatingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SuccessPageViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel()  {

    fun updateRating(
        id: Int,
        rate: String
    ) : LiveData<Resource<UpdateRatingResponse>> =
        repository.updateRating(id, rate).asLiveData()

    fun deleteTrolleyChecked(): Int {
        return repository.deleteCheckedTrolley()
    }
}