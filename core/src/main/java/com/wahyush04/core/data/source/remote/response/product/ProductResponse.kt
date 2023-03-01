package com.wahyush04.core.data.source.remote.response.product

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val success : Success
)

data class Success(
    val status : Int,
    val data : ArrayList<DataListProduct>,
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


data class ProductResponsePaging(

    @field:SerializedName("success")
    val success: SuccessPaging? = null
)

data class DataListProductPaging(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("name_product")
    val nameProduct: String? = null,

    @field:SerializedName("harga")
    val harga: String? = null,

    @field:SerializedName("size")
    val size: String? = null,

    @field:SerializedName("rate")
    val rate: Int? = null,

    @field:SerializedName("weight")
    val weight: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("stock")
    val stock: Int? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("desc")
    val desc: String? = null
)

data class SuccessPaging(

    @field:SerializedName("total_row")
    val totalRow: Int? = null,

    @field:SerializedName("data")
    val data: List<DataListProductPaging>,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)




