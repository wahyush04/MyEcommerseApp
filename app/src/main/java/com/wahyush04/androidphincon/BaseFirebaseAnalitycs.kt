package com.wahyush04.androidphincon

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.wahyush04.core.Constant

open class BaseFirebaseAnalytics {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun onLoadScreen(screen_name : String,screen_class: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screen_class)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun onClickButtonLogin(email: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login")
        bundle.putString(Constant.FK_EMAIL, email)
        bundle.putString(Constant.BUTTON_NAME, "Login")
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickButtonSignUp() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Login")
        bundle.putString(Constant.BUTTON_NAME, "Sign Up")
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickToLogin(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign Up")
        bundle.putString(Constant.BUTTON_NAME, "Login")
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickCameraIcon(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign Up")
        bundle.putString(Constant.BUTTON_NAME, "Icon Photo")
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onChangeImage(imageFrom : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign Up")
        bundle.putString(Constant.FK_IMAGE, "Icon Photo")
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onClickButtonSignUp(
        image: String,
        email: String,
        name: String,
        phone: String,
        gender: String)
    {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign Up")
        bundle.putString(Constant.FK_IMAGE, image)
        bundle.putString(Constant.FK_EMAIL, email)
        bundle.putString(Constant.FK_NAME, name)
        bundle.putString(Constant.FK_PHONE, phone)
        bundle.putString(Constant.FK_GENDER, gender)
        bundle.putString(Constant.BUTTON_NAME, "Sign Up")
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

}