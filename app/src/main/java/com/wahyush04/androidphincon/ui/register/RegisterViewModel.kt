package com.wahyush04.androidphincon.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.core.api.ApiConfig
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.register.RegisterResponse
import com.wahyush04.core.helper.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val registerResponse =  MutableLiveData<RegisterResponse>()
    val errorMessage = MutableLiveData<ErrorResponse>()

    private var _registerError = MutableLiveData<Event<ErrorResponse>>()
    val registerError: LiveData<Event<ErrorResponse>> = _registerError

    fun register(name:String, email: String, password: String, phone : String, gender : Int){
        val client = ApiConfig.getApiService().userRegister("TuIBt77u7tZHi8n7WqUC", name, email, password, phone, gender)
        client.enqueue(object  : Callback <RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                   registerResponse.postValue(response.body())
                }else{
                    val jObjError = JSONObject(response.errorBody()!!.string()).toString()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(jObjError, JsonObject::class.java)
                    val error = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    _registerError.value = Event(error)
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("Retrofit", "Retrofit Gagal")
            }
        })
    }

    fun getRegisterResponse(): LiveData<RegisterResponse> {
        return registerResponse
    }

    fun getErrorBody(): LiveData<ErrorResponse> {
        return errorMessage
    }
}