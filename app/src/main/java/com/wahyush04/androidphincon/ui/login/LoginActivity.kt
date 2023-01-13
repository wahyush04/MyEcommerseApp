package com.wahyush04.androidphincon.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        sharedPreferences = PreferenceHelper(this)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

//        binding.edtEmail.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                setEmailEditText()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//        })

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
                val token = data.success.access_token
                val id = data.success.data_user.id
                val name = data.success.data_user.name
                val emailUser = data.success.data_user.email
                val phone = data.success.data_user.phone
                val gender = data.success.data_user.gender
                if (status == 200){
                    sharedPreferences.put(token, id, name, emailUser, phone, gender)
                    sharedPreferences.putLogin(Constant.IS_LOGIN, true)
                    Toast.makeText(this,data.success.message,Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
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
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
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