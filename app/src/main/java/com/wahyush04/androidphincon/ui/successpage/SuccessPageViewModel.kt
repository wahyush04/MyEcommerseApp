package com.wahyush04.androidphincon.ui.successpage

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.core.data.updaterating.UpdateRatingResponse
import com.wahyush04.core.helper.PreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuccessPageViewModel(application: Application) : AndroidViewModel(application) {
    val updateRatingResponse = MutableLiveData<UpdateRatingResponse>()

    fun setUpdateResponse(pref : PreferenceHelper, context : Context, id : Int, rate : String ){
        val client = ApiConfig.getApiService(pref, context).updateRating(id, rate)
        client.enqueue(object : Callback <UpdateRatingResponse>{
            override fun onResponse(
                call: Call<UpdateRatingResponse>,
                response: Response<UpdateRatingResponse>
            ) {
                if (response.isSuccessful){
                    updateRatingResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<UpdateRatingResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getUpdateResponse() : LiveData<UpdateRatingResponse>{
        return updateRatingResponse
    }
}