package com.wahyush04.androidphincon.ui.detailproduct

import androidx.lifecycle.*
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.AddRemoveFavResponse
import com.wahyush04.core.data.product.ProductResponse
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


}