package com.wahyush04.core.data.updatestock

class UpdateStockRequestBody(
	val data_stock: List<DataStockItem>
)
class DataStockItem(
	val id_product: String,
	val stock: Int
)

