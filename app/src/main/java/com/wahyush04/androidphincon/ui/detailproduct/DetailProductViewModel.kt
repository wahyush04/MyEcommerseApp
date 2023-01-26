package com.wahyush04.androidphincon.ui.detailproduct

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.detailproduct.DetailProductResponse
import com.wahyush04.core.data.favorite.FavoriteResponse
import com.wahyush04.core.helper.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductViewModel(application: Application) : AndroidViewModel(application) {
    val detailProduct = MutableLiveData<DetailProductResponse>()
    val addFavoriteResponse = MutableLiveData<FavoriteResponse>()
    val removeFavoriteResponse = MutableLiveData<FavoriteResponse>()

    fun setDetailProduct(pref : PreferenceHelper, context : Context, id : Int, id_user : Int){
        val client = ApiConfig.getApiService(pref, context).getDetailProduct(id, id_user)
        client.enqueue(object : Callback <DetailProductResponse>{
            override fun onResponse(
                call: Call<DetailProductResponse>,
                response: Response<DetailProductResponse>
            ) {
                if (response.isSuccessful){
                    detailProduct.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailProductResponse>, t: Throwable) {
                Log.d("Failed", "Retrofit Error")
            }

        })
    }

    fun getDetailProduct(): LiveData<DetailProductResponse>{
        return detailProduct
    }

    fun addFavorite(pref : PreferenceHelper, context : Context,id_product : Int, id_user : Int){
        val client = ApiConfig.getApiService(pref, context).addFavorite(id_product, id_user)
        client.enqueue(object  : Callback <FavoriteResponse>{
            override fun onResponse(
                call: Call<FavoriteResponse>,
                response: Response<FavoriteResponse>
            ) {
                if (response.isSuccessful){
                    addFavoriteResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun removeFavorite(pref : PreferenceHelper, context : Context,id_product : Int, id_user : Int){
        val client = ApiConfig.getApiService(pref, context).removeFavorite(id_product, id_user)
        client.enqueue(object  : Callback <FavoriteResponse>{
            override fun onResponse(
                call: Call<FavoriteResponse>,
                response: Response<FavoriteResponse>
            ) {
                removeFavoriteResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<FavoriteResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getAddFavResponse(): LiveData<FavoriteResponse>{
        return addFavoriteResponse
    }

    fun getremoveFavResponse(): LiveData<FavoriteResponse>{
        return removeFavoriteResponse
    }

}