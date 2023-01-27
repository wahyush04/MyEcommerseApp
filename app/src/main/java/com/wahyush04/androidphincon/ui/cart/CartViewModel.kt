package com.wahyush04.androidphincon.ui.cart

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.updatestock.DataStockItem
import com.wahyush04.core.data.updatestock.UpdateStockRequestBody
import com.wahyush04.core.data.updatestock.UpdateStockResponse
import com.wahyush04.core.database.ProductDao
import com.wahyush04.core.database.ProductDatabase
import com.wahyush04.core.database.ProductEntity
import com.wahyush04.core.helper.Event
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private var favDao: ProductDao
    private val cartRepository : CartRepository = CartRepository(application)
    val updateStockResponse = MutableLiveData<UpdateStockResponse>()

    private var _loginError = MutableLiveData<Event<ErrorResponse>>()
    val loginError: LiveData<Event<ErrorResponse>> = _loginError
    init {
        favDao = ProductDatabase.getInstance(application).favoriteDao()
    }

    fun getTrolley(): LiveData<List<ProductEntity>> {
        return favDao.getProduct()
    }

    fun insert(data: ProductEntity){
        cartRepository.addTrolley(data)
    }

    fun isCheck(id : Int) : Int{
        return cartRepository.isCheck(id)
    }

    fun delete(data : ProductEntity){
        cartRepository.deleteTrolley(data)
    }

    suspend fun totalTrolley() : Int? {
        return cartRepository.countItems()
    }

    fun updateQuantity(quantity: Int, id: Int): Int = cartRepository.updateQuantity(quantity,id)

//    fun checkQuantity(id: Int): Int = cartRepository.isCheck(id, stat)

    fun checkBox( id: Int,state : Boolean) : Int =  cartRepository.checkBox(id, state)

    fun deleteCart(data : ProductEntity) = viewModelScope.launch(Dispatchers.IO) { cartRepository.deleteTrolley(data) }

    fun setBuyProduct(requestBody: UpdateStockRequestBody, preferences : PreferenceHelper, context: Context){
        Log.d("requestBody",  requestBody.toString())
        val client = ApiConfig.getApiService(preferences, context).buyProduct(requestBody)
        client.enqueue(object : Callback<UpdateStockResponse> {
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