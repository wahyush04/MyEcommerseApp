package com.wahyush04.core.data.refreshtoken

data class RefreshTokenResponse(
    val success : Success
)

data class Success(
    val status : Int,
    val access_token : String,
    val refresh_token : String,
    val message : String
)
