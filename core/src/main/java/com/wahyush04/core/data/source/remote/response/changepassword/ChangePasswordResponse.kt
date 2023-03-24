package com.wahyush04.core.data.source.remote.response.changepassword

import com.google.gson.annotations.SerializedName
import com.wahyush04.core.data.source.remote.response.register.RegisterSuccess

data class ChangePasswordResponse(
    @field:SerializedName("success")
    val registerSuccess : RegisterSuccess
)