package com.wahyush04.core.data.source.remote.response.login

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class LoginResponse (
    val success : Success
)

@JsonClass(generateAdapter = true)
data class Success(
    val status : Int,
    val data_user : DataUser,
    val access_token : String,
    val refresh_token : String,
    val message : String
)

@JsonClass(generateAdapter = true)
data class DataUser(
    val id : Int,
    val name : String,
    val email : String,
    val phone : String,
    val gender : Int,
    val path : String
)

