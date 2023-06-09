package com.wahyush04.androidphincon.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.databinding.ActivityLoginBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.main.MainActivity
import com.wahyush04.androidphincon.ui.register.RegisterActivity
import com.wahyush04.core.BaseFirebaseAnalytics
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.Result
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.isInternetConnectionAvailable
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private val firebaseAnalytics = BaseFirebaseAnalytics()

    @Inject
    lateinit var preferences: PreferenceHelper
    private lateinit var loadingDialog: LoadingDialog

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
        loadingDialog = LoadingDialog(this@LoginActivity)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.edtEmailLogin.doOnTextChanged { _, _, _, _ ->
            setEmailEditText()
        }

        binding.btnLogin.setOnClickListener {
            if (isInternetConnectionAvailable(this@LoginActivity)) {
                val email = binding.edtEmailLogin.text.toString()
                val password = binding.edtPasswordLogin.text.toString()
                val tokenFcm = preferences.getPreference(Constant.TOKEN_FCM)
                when {
                    email.isEmpty() -> {
                        binding.emailedtlayout.error = "Email is Empty"
                    }
                    password.isEmpty() -> {
                        binding.passwordedtlayout.error = "Password is Empty"
                    }
                    else -> {
                        if (tokenFcm != null) {
                            //GA Slide 5 onClickButtonLogin
                            firebaseAnalytics.onClickButtonLogin(
                                "Login",
                                email,
                                "Login"
                            )
                            login(email, password, tokenFcm)
                        } else {
                            Toast.makeText(this, "Oops, Something when wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "No Internet Connections", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnToSignup.setOnClickListener {
            //GA Slide 5 OnClickButtonSignUp
            firebaseAnalytics.onClickButtonSignUp(
                "Login",
                "Signup"
            )
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setEmailEditText() {
        val email = binding.edtEmailLogin.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailedtlayout.error = "Wrong Email Format"
        } else {
            binding.emailedtlayout.error = null
        }
    }

    override fun onStart() {
        super.onStart()
        getTokenFirebase()
        if (preferences.getIsLogin(Constant.IS_LOGIN)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getTokenFirebase() {
        if (isInternetConnectionAvailable(this)) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                val tokenFcm = task.result
                Log.d("tokenFcm", tokenFcm)
                preferences.putTokenFcm(Constant.TOKEN_FCM, tokenFcm)
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(email: String, password: String, tokenFcm: String) {
        loginViewModel.login(email, password, tokenFcm).observe(this) {
            when (it) {
                is Result.Loading -> {
                    loadingDialog.startLoading()
                }
                is Result.Success -> {
                    loadingDialog.stopLoading()
                    val status = it.data.success.status
                    val accessToken = it.data.success.access_token
                    val refreshToken = it.data.success.refresh_token
                    val id = it.data.success.data_user.id
                    val name = it.data.success.data_user.name
                    val emailUser = it.data.success.data_user.email
                    val phone = it.data.success.data_user.phone
                    val gender = it.data.success.data_user.gender
                    val image = it.data.success.data_user.path
                    val successMessage = it.data.success.message
                    if (status == 200) {
                        preferences.put(
                            accessToken,
                            refreshToken,
                            id,
                            name,
                            emailUser,
                            phone,
                            gender,
                            image
                        )
                        preferences.putLogin(Constant.IS_LOGIN, true)
                        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
                is Result.Error -> {
                    loadingDialog.stopLoading()
                    val err = it.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                    val gson = Gson()
                    val jsonObject = gson.fromJson(err, JsonObject::class.java)
                    val errorResponse = gson.fromJson(jsonObject, ErrorResponse::class.java)
                    val messageErr = errorResponse.error.message
                    Toast.makeText(this, messageErr, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    loadingDialog.stopLoading()
                    Toast.makeText(
                        this@LoginActivity,
                        "Oops, Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //GA Slide 5 OnLoadScreen
        firebaseAnalytics.onLoadScreen(
            "Login",
            this.javaClass.simpleName)
    }

}