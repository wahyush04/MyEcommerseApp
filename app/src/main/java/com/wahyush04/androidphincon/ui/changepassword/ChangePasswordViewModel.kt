package com.wahyush04.androidphincon.ui.changepassword

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.changepassword.ChangePasswordResponse
import com.wahyush04.core.helper.Event
import com.wahyush04.core.helper.PreferenceHelper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {
    val changePasswordResponse = MutableLiveData<ChangePasswordResponse>()

    private var _registerError = MutableLiveData<Event<ErrorResponse>>()
    val registerError: LiveData<Event<ErrorResponse>> = _registerError

    fun changePassword(id : String, password: String, new_password : String, confirm_password : String, pref : PreferenceHelper, context : Context){
        val client = ApiConfig.getApiService(pref, context).userChangePassword(id, password, new_password, confirm_password)
        client.enqueue(object : Callback <ChangePasswordResponse>{
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                if (response.isSuccessful){
                    changePasswordResponse.postValue(response.body())
                }else{
                    val jObjError = JSONObject(response.errorBody()!!.string()).toString()
                    val gson = Gson()
                    val jsonObject = gson.fromJson(jObjError, JsonObject::class.java)
                    val error = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    _registerError.value = Event(error)
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Log.d("Failed", "Retrofit Error")
            }

        })
    }

    fun getChangePasswordResponse(): LiveData<ChangePasswordResponse> {
        return changePasswordResponse
    }

}