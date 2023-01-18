package com.wahyush04.androidphincon.ui.main.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.changeimage.ChangeImageResponse
import com.wahyush04.core.helper.PreferenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileViewModel : ViewModel() {

    val changeImageResponse = MutableLiveData<ChangeImageResponse>()

    fun changeImage( id : String, image : MultipartBody.Part, pref : PreferenceHelper, context : Context){
        val client = ApiConfig.getApiService(pref, context).changeImage(id.toRequestBody("text/plain".toMediaType()), image)
        client.enqueue(object : Callback<ChangeImageResponse>{
            override fun onResponse(
                call: Call<ChangeImageResponse>,
                response: Response<ChangeImageResponse>
            ) {
                if (response.isSuccessful){
                    changeImageResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ChangeImageResponse>, t: Throwable) {
                Log.d("Failed", "Failed get data")
            }

        })
    }

    fun getChangeImageResponse() : LiveData<ChangeImageResponse>{
        return changeImageResponse
    }
}