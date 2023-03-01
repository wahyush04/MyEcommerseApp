package com.wahyush04.androidphincon.ui.changepassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.databinding.ActivityChangePasswordBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.core.BaseFirebaseAnalytics
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.Result
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val changePasswordViewModel: ChangePasswordViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceHelper
    private lateinit var loadingDialog: LoadingDialog
    private val firebaseAnalytics = BaseFirebaseAnalytics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this@ChangePasswordActivity)
        
        setAppBar()
        binding.ivBack.setOnClickListener {
            //GA Slide 29 onCLickBackIcon
            firebaseAnalytics.onClickButton(
                "Change Password",
                "Back Icon"
            )
            this@ChangePasswordActivity.onBackPressed()
        }

        binding.btnSaveNewPassword.setOnClickListener {
            val id = preferences.getPreference(Constant.ID).toString()
            val accessToken = preferences.getPreference(Constant.TOKEN).toString()

            val password = binding.edtOldPassword.text.toString()
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirmNewPassword.text.toString()
            binding.oldpasswordedtlayout.isErrorEnabled = false
            binding.newpasswordedtlayout.isErrorEnabled = false
            binding.confirmpasswordedtlayout.isErrorEnabled = false
            when {
                password.isEmpty() -> {
                    binding.oldpasswordedtlayout.error = "Old Password is Empty"
                }
                newPassword.isEmpty() -> {
                    binding.newpasswordedtlayout.error = "Old Password is Empty"
                }
                confirmPassword.isEmpty() -> {
                    binding.confirmpasswordedtlayout.error = "Old Password is Empty"
                }
                newPassword != confirmPassword -> {
                    binding.oldpasswordedtlayout.error = "Old Password is Empty"
                    binding.confirmpasswordedtlayout.error = "Old Password is Empty"
                }else ->{
                changePassword(id, password, newPassword, confirmPassword)
                }
            }

            //GA Slide 29 onClickSaveButton
            firebaseAnalytics.onClickButton(
                "Change Password",
                "Save"
            )
        }
    }

    private fun changePassword(id : String, password: String, newPassword: String, confirmPassword : String){
        changePasswordViewModel.changePassword(
            id.toInt(),
            password,
            newPassword,
            confirmPassword)
            .observe(this){
                when (it){
                    is Result.Loading -> {
                        loadingDialog.startLoading()
                    }
                    is Result.Success -> {
                        loadingDialog.stopLoading()
                        val data = it.data.registerSuccess.message
                        AlertDialog.Builder(this)
                            .setTitle("Change Password Success")
                            .setMessage(data)
                            .setPositiveButton("Ok") { _, _ ->
                                val intent = Intent(
                                    this@ChangePasswordActivity,
                                    MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                            .show()
                    }
                    is Result.Error -> {
                        loadingDialog.stopLoading()
                        try {
                            val err = it.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                            val gson = Gson()
                            val jsonObject = gson.fromJson(err, JsonObject::class.java)
                            val errorResponse =
                                gson.fromJson(jsonObject, ErrorResponse::class.java)
                            val messageErr = errorResponse.error.message
                            AlertDialog.Builder(this)
                                .setTitle("Change Password Failed")
                                .setMessage(messageErr)
                                .setPositiveButton("Ok") { _, _ ->
                                }
                                .show()
                        } catch (e: java.lang.Exception) {
                            val err = it.errorCode
                            Log.d("ErrorCode", "$err")
                        }
                    }
                    else -> {
                        loadingDialog.stopLoading()
                        AlertDialog.Builder(this)
                            .setTitle("Oops, Something when wrong")
                            .setMessage("Please cek your internet connection")
                            .setPositiveButton("Ok") { _, _ ->
                            }
                            .show()
                    }
                }
            }
    }

    private fun setAppBar(){
        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        //GA slide 29 onLoadScreen
        firebaseAnalytics.onLoadScreen(
            "Change Password",
            this.javaClass.simpleName
        )
    }
}