package com.wahyush04.androidphincon.ui.main.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.helper.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel : ViewModel() {

    val productResponse = MutableLiveData<ArrayList<DataListProduct>?>()

    fun getFavoriteProduct(search : String?, id : Int, context : Context, preferences : PreferenceHelper){
        val client = ApiConfig.getApiService(preferences, context).getFavorite(search, id)
        client.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful){
                    productResponse.postValue(response.body()?.success?.data)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.d("Failed", "Failed search data")
            }

        })
    }

    fun getFavoriteProductData(): LiveData<ArrayList<DataListProduct>?>{
        return productResponse
    }
}