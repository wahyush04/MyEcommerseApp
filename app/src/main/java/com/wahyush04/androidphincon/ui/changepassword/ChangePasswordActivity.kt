package com.wahyush04.androidphincon.ui.changepassword

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.MainActivity
import com.wahyush04.androidphincon.databinding.ActivityChangePasswordBinding
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private lateinit var sharedPreferences: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changePasswordViewModel = ViewModelProvider(this)[ChangePasswordViewModel::class.java]
        sharedPreferences = PreferenceHelper(this)

//        val pref : SharedPreferences = this.getSharedPreferences(Constant.PREFKEY, Context.MODE_PRIVATE)

        val pref = getSharedPreferences(Constant.PREFKEY, Context.MODE_PRIVATE)

        Log.d("tokenChangePassword", sharedPreferences.getPreference(Constant.TOKEN).toString())

        binding.btnSaveNewPassword.setOnClickListener {
            val id = sharedPreferences.getPreference(Constant.ID).toString()
            val accessToken = sharedPreferences.getPreference(Constant.TOKEN).toString()
            Log.d("AKSESTOKEN", accessToken)
            val password = binding.edtOldPassword.text.toString()
            val newPassword = binding.edtNewPassword.text.toString()
            val confirmPassword = binding.edtConfirmNewPassword.text.toString()
            changePasswordViewModel.changePassword(accessToken, id, password, newPassword, confirmPassword, sharedPreferences)

            changePasswordViewModel.getChangePasswordResponse().observe(this){ data ->
                val status = data.success.status
                if (status == 200){
                    Toast.makeText(applicationContext, data.success.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }

                changePasswordViewModel.registerError.observe(this){ error ->
                    error.getContentIfNotHandled()?.let {
                        Toast.makeText(applicationContext, it.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}