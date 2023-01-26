package com.wahyush04.core.data.updatestock

data class UpdateStockResponse (
    val success : Success
        )

data class Success(
    val status : Int,
    val message : String
)