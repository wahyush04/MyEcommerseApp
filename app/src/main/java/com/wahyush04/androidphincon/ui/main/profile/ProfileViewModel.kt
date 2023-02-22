package com.wahyush04.androidphincon.ui.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository : IRepository
) : ViewModel() {

    fun changeImage(
        id : String,
        image : MultipartBody.Part
    ) = repository
        .changeImage(id, image)
        .asLiveData()
}