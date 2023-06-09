package com.wahyush04.core.data.source.remote.response.favorite

data class AddRemoveFavResponse(
	val success: Success? = null
)

data class Success(
	val message: String? = null,
	val status: Int? = null
)

