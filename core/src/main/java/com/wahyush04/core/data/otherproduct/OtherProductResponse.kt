package com.wahyush04.core.data.otherproduct

data class FavoriteResponse(
	val success: Success? = null
)

data class Success(
	val message: String? = null,
	val status: Int? = null
)

