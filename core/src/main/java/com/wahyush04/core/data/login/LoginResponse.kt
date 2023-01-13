package com.wahyush04.core.data.login


data class LoginResponse (
    val success : Success
)

data class Success(
    val status : Int,
    val data_user : DataUser,
    val access_token : String,
    val refresh_token : String,
    val message : String
)

data class DataUser(
    val id : Int,
    val name : String,
    val email : String,
    val phone : String,
    val gender : Int
)

