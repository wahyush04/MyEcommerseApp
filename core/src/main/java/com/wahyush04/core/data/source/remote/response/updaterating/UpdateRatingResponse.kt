package com.wahyush04.core.data.source.remote.response.updaterating

data class UpdateRatingResponse (
    val success : Success
)

data class Success(
    val status : Int,
    val message : String
)