package com.wahyush04.androidphincon.ui.changepassword

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.androidphincon.core.repository.Repository
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.helper.Event
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun changePassword(
        id: Int,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) = repository
        .changePassword(
            id,
            oldPassword,
            newPassword,
            confirmPassword)
        .asLiveData()
}