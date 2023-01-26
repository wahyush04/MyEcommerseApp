package com.wahyush04.core.data.product

data class ProductResponse(
    val success : Success
)

data class Success(
    val status : Int,
    val data : ArrayList<DataListProduct>?,
    val message : String
)

data class DataListProduct(
    val id : Int,
    val name_product : String,
    val harga : String,
    val rate : Int,
    val image : String,
    val date : String,
    val stock : Int,
    val size : String,
    val weight : String,
    val type : String,
    val desc : String
)

