package com.wahyush04.androidphincon.ui.register

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.databinding.ActivityRegisterBinding
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.helper.reduceFileImage
import com.wahyush04.core.helper.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    private var getFile: File? = null
    private var imageMultipart: MultipartBody.Part? = null
    private lateinit var currentPhotoPath: String
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        loadingDialog = LoadingDialog(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.edtEmail.doOnTextChanged { _, _, _, _ ->
            setEmailEditText()
        }

        binding.btnAddImage.setOnClickListener {
            selectImageFrom()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtPasswordConfirm.text.toString()
            val name = binding.edtName.text.toString()
            val phone = binding.edtPhone.text.toString()
            binding.emailedtlayout.isErrorEnabled = false
            binding.passwordedtlayout.isErrorEnabled = false
            binding.passwordconfirmedtlayout.isErrorEnabled = false
            binding.nameeditLayout.isErrorEnabled = false
            binding.phoneLayout.isErrorEnabled = false
            when {
                email.isEmpty() -> {
                    binding.emailedtlayout.error = "Email is Empty"
                }
                password.isEmpty() -> {
                    binding.passwordedtlayout.error = "password is Empty"
                }
                confirmPassword.isEmpty() -> {
                    binding.passwordconfirmedtlayout.error = "password is Empty"
                }
                password != confirmPassword -> {
                    binding.passwordedtlayout.error = "Password not match"
                    binding.passwordconfirmedtlayout.error = "Password not match"
                }
                name.isEmpty() -> {
                    binding.nameeditLayout.error = "password is Empty"
                }
                phone.isEmpty() -> {
                    binding.phoneLayout.error = "password is Empty"
                }
                else -> {
                    register()
                }
            }
        }

        binding.btnToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setEmailEditText() {
        val email = binding.edtEmail.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailedtlayout.error = getString(R.string.wrong_email_format)
        } else {
            binding.emailedtlayout.error = null
        }
    }

    private fun selectImageFrom() {
        val items = arrayOf(getString(R.string.camera), getString(R.string.galllery))
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.select_image))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> startTakePhoto()
                    1 -> startGallery()
                }
            }
            .show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        com.wahyush04.core.helper.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@RegisterActivity,
                "com.wahyush04.androidphincon",
                it
            )
            currentPhotoPath = it.absolutePath
            Log.d("imagePath", currentPhotoPath)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            Log.e("IntentCamera", getFile.toString())

            val result = BitmapFactory.decodeFile(myFile.path)

            val rotatedimage = rotateBitmap(result)

            binding.ivPhotoProfile.setImageBitmap(rotatedimage)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@RegisterActivity)

            getFile = myFile
            Log.e("IntentGallery", getFile.toString())

            binding.ivPhotoProfile.setImageURI(selectedImg)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(180f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    private fun register() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            imageMultipart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )
        }
        val name = binding.edtName.text.toString().toRequestBody()
        val email = binding.edtEmail.text.toString().toRequestBody()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtPasswordConfirm.text.toString()
        val phone = binding.edtPhone.text.toString().toRequestBody()
        val genderId = if (binding.rbMale.isChecked) {
            0
        } else {
            1
        }

        if (password == confirmPassword) {
            registerViewModel.register(
                name,
                email,
                password.toRequestBody(),
                phone,
                genderId,
                imageMultipart
            ).observe(this@RegisterActivity) {
                when (it) {
                    is Resource.Loading -> {
                        loadingDialog.startLoading()
                    }
                    is Resource.Success -> {
                        loadingDialog.stopLoading()
                        val dataMessages = it.data?.success?.message
                        AlertDialog.Builder(this@RegisterActivity)
                            .setTitle("Register Success")
                            .setMessage(dataMessages)
                            .setPositiveButton("Ok") { _, _ ->
                                Toast.makeText(
                                    this,
                                    getString(R.string.register_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                            .show()

                    }
                    is Resource.Error -> {
                        loadingDialog.stopLoading()
                        val err =
                            it.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(err, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponse::class.java)
                        val messageErr = errorResponse.error.message
                        AlertDialog.Builder(this@RegisterActivity)
                            .setTitle("Gagal")
                            .setMessage(messageErr)
                            .setPositiveButton("Ok") { _, _ ->
                            }
                            .show()
                        val errCode = it.errorCode
                        Log.d("errorCode", "$errCode")
                    }
                    is Resource.Empty -> {
                        Log.d("Empty Data", "Empty")
                    }
                    else -> {

                    }
                }
            }
        } else {
            loadingDialog.stopLoading()
            binding.passwordedtlayout.error = getString(R.string.password_not_match)
            binding.passwordconfirmedtlayout.error = getString(R.string.password_not_match)
            Toast.makeText(
                applicationContext,
                getString(R.string.password_not_match),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}