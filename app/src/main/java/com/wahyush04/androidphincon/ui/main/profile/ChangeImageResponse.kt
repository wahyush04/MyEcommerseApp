package com.wahyush04.androidphincon.ui.main.profile

data class changeImageResponse (
    val success : Success,
)

data class Success(
    val status : Int,
    val path : String,
    val message : String

)