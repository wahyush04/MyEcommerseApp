package com.wahyush04.core.data

data class ErrorResponse(
    val error : Error
)
data class Error(
    val status : Int,
    val message : String
)
