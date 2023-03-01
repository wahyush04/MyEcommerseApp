package com.wahyush04.utils

import com.wahyush04.core.data.source.remote.response.changeimage.ChangeImageResponse
import com.wahyush04.core.data.source.remote.response.changepassword.ChangePasswordResponse
import com.wahyush04.core.data.source.remote.response.detailproduct.Data
import com.wahyush04.core.data.source.remote.response.detailproduct.DetailProductResponse
import com.wahyush04.core.data.source.remote.response.detailproduct.ImageProductItem
import com.wahyush04.core.data.source.remote.response.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.source.remote.response.login.DataUser
import com.wahyush04.core.data.source.remote.response.login.LoginResponse
import com.wahyush04.core.data.source.remote.response.login.Success
import com.wahyush04.core.data.source.remote.response.product.*
import com.wahyush04.core.data.source.remote.response.register.RegisterResponse
import com.wahyush04.core.data.source.remote.response.register.RegisterSuccess
import com.wahyush04.core.data.source.remote.response.updaterating.UpdateRatingResponse
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockResponse


object DataDummy {
    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            Success(
                200,
                DataUser(
                    1,
                    "wahyu",
                    "wahyu@gmail.com",
                    "0987345",
                    1,
                    "path"
                ),
                "accessToken",
                "refreshToken",
                "message"
            )
        )
    }

    fun generateRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            registerSuccess = RegisterSuccess(
                200,
                "message"
            )
        )
    }


    fun generateProductListResponse(): ProductResponse {
        val listProduct = ArrayList<DataListProduct>()
        listProduct.add(
            DataListProduct(
                1,
                "name_product",
                "harga",
                5,
                "Image",
                "date",
                100,
                "size",
                "weight",
                "type",
                "desc"
            )
        )
        listProduct.add(
            DataListProduct(
                1,
                "name_product",
                "harga",
                5,
                "Image",
                "date",
                100,
                "size",
                "weight",
                "type",
                "desc"
            )
        )
        listProduct.add(
            DataListProduct(
                1,
                "name_product",
                "harga",
                5,
                "Image",
                "date",
                100,
                "size",
                "weight",
                "type",
                "desc"
            )
        )
        return ProductResponse(
            Success(
                status = 200,
                message = "success",
                data = listProduct
            )
        )
    }

    fun generateChangePasswordResponse(): ChangePasswordResponse {
        return ChangePasswordResponse(
            RegisterSuccess(
                200,
                "success"
            )
        )
    }

    fun generateChangeImageResponse(): ChangeImageResponse {
        return ChangeImageResponse(
            com.wahyush04.core.data.source.remote.response.changeimage.Success(
                201,
                "path",
                "message"
            )
        )
    }

    fun generateDetailProductResponse(): DetailProductResponse {
        val imageProductItem = listOf(
            ImageProductItem("image_name", "title_product"),
            ImageProductItem("image_name", "title_product")
        )
        return DetailProductResponse(
            com.wahyush04.core.data.source.remote.response.detailproduct
                .Success(
                    status = 200,
                    message = "message",
                    data = Data(
                        "1",
                        "date",
                        "image",
                        "name_product",
                        "harga",
                        "size",
                        5,
                        "weight",
                        imageProductItem,
                        100,
                        "type",
                        "desc",
                        false,
                    )
                )
        )
    }

    fun generateAddFavoriteResponse(): AddRemoveFavResponse {
        return AddRemoveFavResponse(
            com.wahyush04.core.data.source.remote.response.favorite.Success("message", 201)
        )
    }

    fun generateUpdateStockResponse(): UpdateStockResponse {
        return UpdateStockResponse(
            com.wahyush04.core.data.source.remote.response.updatestock.Success(200, "message")
        )
    }

    fun generateUpdateRatingResponse(): UpdateRatingResponse {
        return UpdateRatingResponse(
            com.wahyush04.core.data.source.remote.response.updaterating.Success(201, "message")
        )
    }

    fun generateProductResponsePaging(): ProductResponsePaging {
        val listProduct = ArrayList<DataListProductPaging>()
        listProduct.add(
            DataListProductPaging(
                "date",
                "Image",
                "name_product",
                "harga",
                "size",
                5,
                "weight",
                100,
                20,
                "type",
                "desc"
            )
        )
        listProduct.add(
            DataListProductPaging(
                "date",
                "Image",
                "name_product",
                "harga",
                "size",
                5,
                "weight",
                100,
                20,
                "type",
                "desc"
            )
        )
        listProduct.add(
            DataListProductPaging(
                "date",
                "Image",
                "name_product",
                "harga",
                "size",
                5,
                "weight",
                100,
                20,
                "type",
                "desc"
            )
        )

        return ProductResponsePaging(
            com.wahyush04.core.data.source.remote.response.product
                .SuccessPaging(
                5,
                    listProduct,
                    "message",
                    200
                )
        )
    }

    fun generateDataListProductPaging() : List<DataListProductPaging>{
        val dataList = mutableListOf<DataListProductPaging>()
        for (i in 1..5) {
            val data = DataListProductPaging(
                "date",
                "Image",
                "name_product",
                "harga",
                "size",
                5,
                "weight",
                100,
                20,
                "type",
                "desc"
            )
            dataList.add(data)
        }
        return dataList
    }

}