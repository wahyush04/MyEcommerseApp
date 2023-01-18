package com.wahyush04.androidphincon.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.api.AuthApiConfig
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.login.LoginRequest
import com.wahyush04.core.data.login.LoginResponse
import com.wahyush04.core.helper.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val userDetail = MutableLiveData<LoginResponse>()
    val errorMessage = MutableLiveData<ErrorResponse>()

    private var _loginError = MutableLiveData<Event<ErrorResponse>>()
    val loginError: LiveData<Event<ErrorResponse>> = _loginError

    fun login(email: String, password:String) {
        val request = LoginRequest()
        request.email = email
        request.password = password
        val client = AuthApiConfig.getApiService().userLogin("TuIBt77u7tZHi8n7WqUC", email, password)
        client.enqueue(object : Callback <LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    userDetail.postValue(response.body())
                }else{
                    val jObjError = JSONObject(response.errorBody()!!.string()).toString()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(jObjError, JsonObject::class.java)
                    val error = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    _loginError.value = Event(error)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            }
        })
    }

    fun getDetailLogin(): LiveData<LoginResponse>{
        return userDetail
    }
}