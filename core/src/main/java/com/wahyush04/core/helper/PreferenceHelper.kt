package com.wahyush04.core.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.wahyush04.core.Constant

class PreferenceHelper(context: Context) {
    private val PREF = "loginData"
    private val pref : SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    val editor : SharedPreferences.Editor = pref.edit()
    private val appContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun put(token : String, id : Int, name: String, email: String, phone: String, gender: Int ){
        editor.apply{
            putString(Constant.TOKEN, token)
            putString(Constant.ID, id.toString())
            putString(Constant.NAME, name)
            putString(Constant.EMAIL, email)
            putString(Constant.PHONE, phone)
            putString(Constant.GENDER, gender.toString())
        }
    }

    fun putLogin(key: String, isLogin : Boolean){
        editor.putBoolean(key, isLogin)
            .apply()
    }

    fun getIsLogin(key: String) : Boolean{
        return pref.getBoolean(key, false)
    }

    fun getToken(key: String) : String? {
        return  pref.getString(key, null)
    }

    fun getPreference(key: String) : String? {
        return  pref.getString(key, null)
    }

    fun clear(){
        editor.clear().apply()
    }
}