package com.wahyush04.core.data.updatestock

import com.wahyush04.core.database.DataTrolley

class UpdateStockRequestBody(
	val data_stock: List<DataStockItem>
)
class DataStockItem(
	val id_product: String,
	val stock: Int
)


class UpdateStockRequestBodyReq(
	val data_stock: List<DataTrolley>
)
class DataStockItemReq(
	val id: String,
	val stock: Int
)

