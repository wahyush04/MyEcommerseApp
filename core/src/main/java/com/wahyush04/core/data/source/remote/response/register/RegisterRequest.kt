package com.wahyush04.core.data.source.remote.response.register

data class RegisterRequest (
    val name : String,
    val email : String,
    val password : String,
    val phone : String,
    val gender : Int,
//    val image: Image
)