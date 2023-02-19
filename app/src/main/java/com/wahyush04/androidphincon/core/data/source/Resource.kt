package com.wahyush04.androidphincon.core.data.source

import okhttp3.ResponseBody

sealed class Resource<out T>(val data: T? = null, val message: String? = null) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error(val isNetworkError: Boolean, val errorCode: Int, val errorBody: ResponseBody?) : Resource<Nothing>()
    class Loading<out T>(data: T? = null) : Resource<T>(data)
    class Empty : Resource<Nothing>()
}


sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
