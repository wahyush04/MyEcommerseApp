package com.wahyush04.androidphincon.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.core.data.Result
import com.wahyush04.core.data.source.remote.response.login.LoginResponse
import com.wahyush04.core.data.source.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
    ) : ViewModel() {
    fun login(
        email: String,
        password: String,
        tokenFcm: String?
    ): LiveData<Result<LoginResponse>>
    = repository.login(email, password, tokenFcm).asLiveData()
    }