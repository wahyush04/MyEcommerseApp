package com.wahyush04.androidphincon.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.core.data.Result
import com.wahyush04.core.data.source.remote.response.register.RegisterResponse
import com.wahyush04.core.data.source.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        gender: Int,
        image: MultipartBody.Part?,
    ): LiveData<Result<RegisterResponse>> =
        repository.register(
            name,
            email,
            password,
            phone,
            gender,
            image
        ).asLiveData()

}