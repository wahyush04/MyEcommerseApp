package com.wahyush04.core.data.detailproduct



data class DetailProductResponse(
	val success: Success? = null
)


data class Data(
	val id: String? = null,
	val date: String? = null,
	val image: String? = null,
	val name_product: String? = null,
	val harga: String? = null,
	val size: String? = null,
	val rate: Int? = null,
	val weight: String? = null,
	val image_product: List<ImageProductItem>,
	val stock: Int? = null,
	val type: String? = null,
	val desc: String? = null,
	val isFavorite : Boolean? = null
)


data class ImageProductItem(
	val image_product: String? = null,
	val title_product: String? = null
)

data class Success(
	val data: Data? = null,
	val message: String? = null,
	val status: Int? = null
)