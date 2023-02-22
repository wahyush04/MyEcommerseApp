package com.wahyush04.androidphincon.ui.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.core.data.source.local.entity.ProductEntity
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.updatestock.UpdateStockResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
        private val repository: IRepository
) : ViewModel(){

    fun detailProduct(
        idProduk: Int,
        idUser: Int
    ): LiveData<Resource<DetailProductResponse>> =
        repository.detailProduct(idProduk, idUser).asLiveData()

    fun addFavorite(
        idProduk: Int,
        idUser: Int
    ): LiveData<Resource<AddRemoveFavResponse>> =
        repository.addFavorite(idProduk, idUser).asLiveData()

    fun removeFavorite(
        idProduk: Int,
        idUser: Int
    ): LiveData<Resource<AddRemoveFavResponse>> =
        repository.removeFavorite(idProduk, idUser).asLiveData()
    fun getOtherProduk(
        idUser: Int
    ): LiveData<Resource<ProductResponse>> =
        repository.getOtherProduk(idUser).asLiveData()

    fun getHistoryProduk(
        idUser: Int
    ): LiveData<Resource<ProductResponse>> =
        repository.getHistoryProduk(idUser).asLiveData()

    fun buyProduct(
        requestBody: UpdateStockRequestBody
    ) : LiveData<Resource<UpdateStockResponse>> =
        repository.buyProduct(requestBody).asLiveData()

    fun insertTrolley(
        data : ProductEntity) =
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