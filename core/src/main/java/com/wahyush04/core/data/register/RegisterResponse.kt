package com.wahyush04.core.data.register

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RegisterResponse (
    val success : Success
)

@JsonClass(generateAdapter = true)
data class Success(
    val status : Int,
    val message : String
)