package com.wahyush04.androidphincon.ui.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.core.data.Result
import com.wahyush04.core.data.source.local.entity.ProductEntity
import com.wahyush04.core.data.source.remote.response.detailproduct.DetailProductResponse
import com.wahyush04.core.data.source.remote.response.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.source.remote.response.product.ProductResponse
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.source.remote.response.updatestock.UpdateStockResponse
import com.wahyush04.core.data.source.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
        private val repository: IRepository
) : ViewModel(){

    fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): LiveData<Result<DetailProductResponse>> =
        repository.detailProduct(idProduk, idUser).asLiveData()

    fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): LiveData<Result<AddRemoveFavResponse>> =
        repository.addFavorite(idProduk, idUser).asLiveData()

    fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): LiveData<Result<AddRemoveFavResponse>> =
        repository.removeFavorite(idProduk, idUser).asLiveData()
    fun getOtherProduk(
        idUser: Int
    ): LiveData<Result<ProductResponse>> =
        repository.getOtherProduk(idUser).asLiveData()

    fun getHistoryProduk(
        idUser: Int
    ): LiveData<Result<ProductResponse>> =
        repository.getHistoryProduk(idUser).asLiveData()

    fun buyProduct(
        requestBody: UpdateStockRequestBody
    ) : LiveData<Result<UpdateStockResponse>> =
        repository.buyProduct(requestBody).asLiveData()

    fun insertTrolley(
        data : ProductEntity
    ) =
        repository.insertTrolley(data)
    fun isTrolley(id : Int): Int {
        return repository.isTrolley(id)
    }

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity


    init {
        _quantity.value = 1
    }

    fun increaseQuantity(stock: Int?) {
        if (_quantity.value!! < stock!!) {
            _quantity.value = _quantity.value?.plus(1)
        }
    }

    fun decreaseQuantity() {
        if (quantity.value == 1) {
            _quantity.value = 1
        } else {
            _quantity.value = _quantity.value?.minus(1)
        }
    }

    fun updateQuantity(
        quantity: Int,
        id: Int,
        newTotalHarga : Int): Int =
        repository.updateQuantityTrolley(quantity,id, newTotalHarga)

}