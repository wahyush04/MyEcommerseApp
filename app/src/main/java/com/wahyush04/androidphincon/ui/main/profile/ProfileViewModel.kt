package com.wahyush04.androidphincon.ui.main.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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