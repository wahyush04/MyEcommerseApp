package com.wahyush04.core.data.source.remote.response.register

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RegisterResponse (
    @field:SerializedName("success")
    val registerSuccess : RegisterSuccess
)

@JsonClass(generateAdapter = true)
data class RegisterSuccess(
    val status : Int,
    val message : String
)