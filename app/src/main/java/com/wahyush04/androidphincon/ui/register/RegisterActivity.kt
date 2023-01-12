package com.wahyush04.androidphincon.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.databinding.ActivityRegisterBinding
import com.wahyush04.androidphincon.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.view.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.edtEmail.addTextChangedListener(object  : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEmailEditText()
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.btnRegister.setOnClickListener {
            showLoading(true)
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
//            val confirmPassword = binding.edtPasswordConfirm.text.toString()
            val phone = binding.edtPhone.text.toString()
            val genderId = if (binding.rbMale.isChecked){ 0 }else{ 1 }

            if (email.isEmpty()){
                showLoading(false)
                binding.emailedtlayout.error = "Insert Email"
            }

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                registerViewModel.register(name, email, password, phone, genderId)
                registerViewModel.getRegisterResponse().observe(this){ data ->
                    AlertDialog.Builder(this)
                        .setTitle("Register Success")
                        .setMessage("Register is successfully")
                        .setPositiveButton("Login") { _, _ ->
                            val status = data.success.status.toInt()
                            if (status == 201){
                                Toast.makeText(this,"Register Berhasil",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                            }
                        }
                        .show()
                }
                registerViewModel.getErrorBody().observe(this){
                    Toast.makeText(this,it.error.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.emailedtlayout.error = "Wrong Email Format"
                Toast.makeText(this,"Register Gagal",Toast.LENGTH_SHORT).show()
            }
            showLoading(false)
        }

        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
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

//    private fun setPasswordEditText(){
//        val password = binding.edtPassword.text.toString()
//        val passwordConfirm = binding.edtPasswordConfirm.text.toString()
//        if (password != passwordConfirm){
//            binding.edtPassword.error = "Password ot match"
//            binding.edtPasswordConfirm.error = "Password ot match"
//        }
//            binding.edtPassword.error = null
//            binding.edtPasswordConfirm.error = null
//    }
}