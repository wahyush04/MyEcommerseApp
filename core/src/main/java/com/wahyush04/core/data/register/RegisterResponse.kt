package com.wahyush04.core.data.register

data class RegisterResponse (
    val success : Success
)

data class Success(
    val status : String,
    val message : String
)