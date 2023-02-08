package com.wahyush04.core.data.updatestock

class UpdateStockRequestBody(
	val id_user : String,
	val data_stock: List<DataStockItem>
)
class DataStockItem(
	val id_product: String,
	val stock: Int
)


//class UpdateStockRequestBodyReq(
//	val data_stock: List<DataTrolley>
//)
//class DataStockItemReq(
//	val id: String,
//	val stock: Int
//)

