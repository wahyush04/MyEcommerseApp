package com.wahyush04.androidphincon.ui.login

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.core.Constant
import com.wahyush04.androidphincon.MainActivity
import com.wahyush04.androidphincon.databinding.ActivityLoginBinding
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.androidphincon.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedPreferences: PreferenceHelper

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        sharedPreferences = PreferenceHelper(this)
        Log.e("tokenLogin", sharedPreferences.getPreference(Constant.TOKEN).toString())

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.edtEmail.doOnTextChanged { _, _, _, _ ->
            setEmailEditText()
        }

        binding.btnLogin.setOnClickListener {
            showLoading(true)
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            loginViewModel.login(email, password)
            loginViewModel.getDetailLogin().observe(this){ data ->
                val status = data.success.status
                val accessToken = data.success.access_token
                val refreshToken =  data.success.refresh_token
                val id = data.success.data_user.id
                val name = data.success.data_user.name
                val emailUser = data.success.data_user.email
                val phone = data.success.data_user.phone
                val gender = data.success.data_user.gender
                val image =  data.success.data_user.path
                if (status == 200){
                    sharedPreferences.put(accessToken, refreshToken, id, name, emailUser, phone, gender, image)
                    sharedPreferences.putLogin(Constant.IS_LOGIN, true)
                    Toast.makeText(this,data.success.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            loginViewModel.loginError.observe(this){
                it.getContentIfNotHandled()?.let {
                    showLoading(false)
                    Toast.makeText(applicationContext, it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnToSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.loadingDialog.loadingLayout.visibility = View.VISIBLE
        }else{
            binding.loadingDialog.loadingLayout.visibility = View.GONE
        }
    }

    private fun setEmailEditText() {
        val email = binding.edtEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailedtlayout.error = "Wrong Email Format"
        }else{
            binding.emailedtlayout.error = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (sharedPreferences.getIsLogin(Constant.IS_LOGIN)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        showLoading(false)
    }
}