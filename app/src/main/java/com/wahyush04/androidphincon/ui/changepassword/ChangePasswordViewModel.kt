package com.wahyush04.androidphincon.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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