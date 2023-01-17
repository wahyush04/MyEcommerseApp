package com.wahyush04.core.data.changeimage

data class ChangeImageResponse (
    val success : Success,
)

data class Success(
    val status : Int,
    val path : String,
    val message : String

)