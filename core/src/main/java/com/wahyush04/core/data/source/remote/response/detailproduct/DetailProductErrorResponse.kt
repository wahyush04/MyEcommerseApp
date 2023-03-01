package com.wahyush04.core.data.source.remote.response.detailproduct

data class DetailProductErrorResponse(
	val error: Error? = null
)

data class Error(
	val message: String? = null,
	val status: Int? = null
)

