package com.wahyush04.core.data.detailproduct

data class DetailProductErrorResponse(
	val error: Error? = null
)

data class Error(
	val message: String? = null,
	val status: Int? = null
)

