package com.wahyush04.androidphincon.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.core.data.register.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun register(
        name: RequestBody,
        email: RequestBody,
        password: RequestBody,
        phone: RequestBody,
        gender: Int,
        image: MultipartBody.Part?,
    ): LiveData<Resource<RegisterResponse>> =
        repository.register(image, email, password, name, phone, gender).asLiveData()

}