package com.wahyush04.core.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.wahyush04.core.Constant
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) {
    private val pref : SharedPreferences = context.getSharedPreferences(Constant.PREFERENCES, Context.MODE_PRIVATE)
    private val editor : SharedPreferences.Editor = pref.edit()
    private val appContext = context.applicationContext
    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun put(access_token : String?, refresh_token : String? ,id : Int?, name: String?, email: String?, phone: String?, gender: Int?, image: String? ){
        editor.apply{
            putString(Constant.TOKEN, access_token)
            putString(Constant.REFRESH_TOKEN, refresh_token)
            putString(Constant.ID, id.toString())
            putString(Constant.NAME, name)
            putString(Constant.EMAIL, email)
            putString(Constant.PHONE, phone)
            putString(Constant.GENDER, gender.toString())
            if (image != null){
                putString((Constant.IMAGE), image)
            }
        }
    }

    fun putLogin(key: String, isLogin : Boolean){
        editor.putBoolean(key, isLogin)
            .apply()
    }

    fun putTokenFcm(key: String, tokenFcm : String){
        editor.putString(key, tokenFcm)
            .apply()
    }


    fun putCheck(isChecked : Boolean){
        editor.putBoolean(Constant.ISCHECK, isChecked)
            .apply()
    }

    fun getIsCheck(key: String) : Boolean{
        return pref.getBoolean(key, false)
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

    fun getLocalId(key: String) : Int {
        return  pref.getInt(key, 1)
    }

    fun changeImage(value: String){
        editor.putString(Constant.IMAGE, value)
        editor.apply()
    }

    fun putLocale(value: String){
        editor.putString(Constant.LOCALE, value)
        editor.apply()
    }

    fun putNewToken(access_token : String, refresh_token : String ){
        editor.apply{
            putString(Constant.TOKEN, access_token)
            putString(Constant.REFRESH_TOKEN, refresh_token)
        }
    }

    fun clearToken(){
        editor.remove(Constant.TOKEN)
        editor.remove(Constant.REFRESH_TOKEN)
        editor.apply()
    }



    fun clear(){
        editor.clear().apply()
    }
}