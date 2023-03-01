package com.wahyush04.androidphincon.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.core.data.Result
import com.wahyush04.core.data.source.local.entity.DataTrolley
import com.wahyush04.core.data.source.local.entity.ProductEntity
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockResponse
import com.wahyush04.core.data.source.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getTrolley(): LiveData<List<ProductEntity>> {
        return repository.getTrolley()
    }

    fun getTrolleyChecked(): List<DataTrolley> {
        return repository.getCheckedTrolley()
    }

    fun deleteTrolleyChecked(): Int {
        return repository.deleteCheckedTrolley()
    }

    fun insert(data: ProductEntity){
        repository.addTrolley(data)
    }

    fun countItemsCheckedTrolley(): Int =
        repository.countItemsCheckedTrolley()

    fun isCheck(id : Int) : Int{
        return repository.isCheckTrolley(id)
    }

    fun delete(data : ProductEntity){
        repository.deleteTrolley(data)
    }

    fun totalTrolley() : Int {
        return repository.countItemsTrolley()
    }

    fun getTotalHarga() : Int {
        return repository.getTotalHargaTrolley()
    }

    fun updateQuantity(quantity: Int, id: Int, newTotalHarga : Int): Int = repository.updateQuantityTrolley(quantity,id, newTotalHarga)

    fun checkAll(state : Int): Int = repository.checkALlTrolley(state)

    fun updateCheck(id: Int, state : Int) : Int  = repository.updateChrolley(id, state)

    fun deleteCart(data : ProductEntity) = repository.deleteTrolley(data)

    fun buyProduct(
        requestBody: UpdateStockRequestBody
    ): LiveData<Result<UpdateStockResponse>> =
        repository.buyProduct(requestBody).asLiveData()


}