package com.wahyush04.core

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

open class BaseFirebaseAnalytics {
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun onLoadScreen(screen_name : String,screen_class: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screen_class)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun onClickButtonLogin(screen_name : String, email: String, button_name:String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.FK_EMAIL, email)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickButtonSignUp(screen_name: String, button_name: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickToLogin(screen_name: String, button_name: String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickCameraIcon(screen_name: String, button_name: String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onChangeImage(imageFrom : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Sign Up")
        bundle.putString(Constant.FK_IMAGE, imageFrom)
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

    fun onPagingScroll(screen_name : String, offset : Int){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putInt(Constant.FK_PAGE, offset)
        firebaseAnalytics.logEvent(Constant.ON_SCROLL, bundle)
    }

    fun onSearch(screen_name : String, search_text : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.FK_SEARCH, search_text)
        firebaseAnalytics.logEvent(Constant.ON_SEARCH, bundle)
    }

    fun onClickProduct(
        screen_name: String?,
        product_name: String?,
        product_price: Double,
        product_rate: Int,
        product_id: Int,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        bundle.putDouble(Constant.FK_PRODUCT_PRICE, product_price)
        bundle.putInt(Constant.FK_PRODUCT_RATE, product_rate)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onClickButton(screen_name : String, button_name : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickSelectItem(screen_name : String, item_name : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.ITEM_NAME, item_name)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onClickSortBy(screen_name : String, sort_by : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.SORT_BY, sort_by)
        firebaseAnalytics.logEvent(Constant.POPUP_SORT, bundle)
    }

    fun onClickItemNotification(screen_name : String, title : String, message : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.TITLE, title)
        bundle.putString(Constant.MESSAGE, message)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onClickDeleteIcon(
        screen_name : String,
        button_name : String,
        total_select_item : Int){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.TOTAL_SELECT_ITEM, total_select_item)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickReadIcon(screen_name : String, button_name : String, total_select_item : Int){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.TOTAL_SELECT_ITEM, total_select_item)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickShareProduct(
        screen_name : String,
        product_name : String,
        product_price : Double,
        product_id : Int,
        button_name : String,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        bundle.putDouble(Constant.FK_PRODUCT_PRICE, product_price)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onCLickLoveIcon(
        screen_name : String,
        button_name : String,
        product_id : Int,
        product_name: String,
        status : String,

    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        bundle.putString(Constant.STATUS, status)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onPopupShow(
        screen_name : String,
        popup : String,
        product_id : Int,
        ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.POPUP, popup)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        firebaseAnalytics.logEvent(Constant.POPUP_DETAIL, bundle)
    }

    fun onClickPlusMinus(
        screen_name : String,
        button_name : String,
        total : Int,
        product_id : Int,
        product_name: String,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.TOTAL, total)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickIconBuyNow(
        screen_name : String,
        button_name : String,
        product_id: Int,
        product_name: String,
        product_price: Int,
        product_total: Int,
        product_total_price: Double,
        payment_method: String

    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        bundle.putInt(Constant.FK_PRODUCT_PRICE, product_price)
        bundle.putInt(Constant.TOTAL, product_total)
        bundle.putDouble(Constant.TOTAL_PRICE, product_total_price)
        bundle.putString(Constant.PAYMENT_METHOD, payment_method)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickBank(
        screen_name : String,
        jenis_pembayaran : String,
        bank : String,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.JENIS_PEMBAYARAN, jenis_pembayaran)
        bundle.putString(Constant.BANK, bank)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onCLickDeleteTrolley(
        screen_name : String,
        button_name : String,
        product_id : Int,
        product_name: String
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onSelectCheckBox(
        screen_name : String,
        product_id : Int,
        product_name: String
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putInt(Constant.FK_PRODUCT_ID, product_id)
        bundle.putString(Constant.FK_PRODUCT_NAME, product_name)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onClickBuyTrolley(
        screen_name : String,
        button_name: String,
        total_rice: Double,
        payment_method: String
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putDouble(Constant.TOTAL_PRICE, total_rice)
        bundle.putString(Constant.PAYMENT_METHOD, payment_method)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onClickButtonSubmit(
        screen_name : String,
        button_name: String,
        rate: Int,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.BUTTON_NAME, button_name)
        bundle.putInt(Constant.RATE, rate)
        firebaseAnalytics.logEvent(Constant.BUTTON_CLICK, bundle)
    }

    fun onChangeLanguage(
        screen_name : String,
        item_name: String,
        language: String,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.ITEM_NAME, item_name)
        bundle.putString(Constant.LANGUAGE, language)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }

    fun onChangeProfileImage(
        screen_name : String,
        image : String,
    ){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen_name)
        bundle.putString(Constant.FK_IMAGE, image)
        firebaseAnalytics.logEvent(Constant.SELECT_ITEM, bundle)
    }
}