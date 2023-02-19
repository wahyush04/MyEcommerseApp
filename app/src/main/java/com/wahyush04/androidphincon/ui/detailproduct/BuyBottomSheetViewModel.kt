package com.wahyush04.androidphincon.ui.detailproduct

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.ui.cart.CartRepository
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.updatestock.UpdateStockResponse
import com.wahyush04.core.helper.Event
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class BuyBottomSheetViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val updateStockResponse = MutableLiveData<UpdateStockResponse>()

    private var _loginError = MutableLiveData<Event<ErrorResponse>>()
    val loginError: LiveData<Event<ErrorResponse>> = _loginError

    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity

    private var _price = MutableLiveData<Int>()
    val price: LiveData<Int> = _price

    private val cartRepository : CartRepository = CartRepository(application)

    init {
        _quantity.value = 1
    }

    fun increaseQuantity(stock: Int?) {
        if (_quantity.value!! < stock!!) {
            _quantity.value = _quantity.value?.plus(1)
        }
    }

    fun isTrolley(id : Int): Int?{
        return cartRepository.isTrolley(id)
    }

    fun decreaseQuantity() {
        if (quantity.value == 1) {
            _quantity.value = 1
        } else {
            _quantity.value = _quantity.value?.minus(1)
        }
    }

    fun setBuyProduct(idProduct : String?, stock : Int, preferences : PreferenceHelper, context: Context){
        val idUser = preferences.getPreference(Constant.ID)
        val requestBody = UpdateStockRequestBody(idUser!!,listOf(DataStockItem(idProduct.toString(), stock)))
        Log.d("requestBody",  requestBody.toString())
        val client = ApiConfig.getApiService(preferences, context).buyProduct(requestBody)
        client.enqueue(object : Callback <UpdateStockResponse>{
            override fun onResponse(
                call: Call<UpdateStockResponse>,
                response: Response<UpdateStockResponse>
            ) {
                if (response.isSuccessful){
                    updateStockResponse.postValue(response.body())
                }else{
                    val jObjError = JSONObject(response.errorBody()!!.string()).toString()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(jObjError, JsonObject::class.java)
                    val error = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    _loginError.value = Event(error)
                }
            }

            override fun onFailure(call: Call<UpdateStockResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUpdateStockresponse(): LiveData<UpdateStockResponse> {
        return updateStockResponse
    }

}
