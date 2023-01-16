package com.wahyush04.androidphincon.ui.register

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ActivityRegisterBinding
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.reduceFileImage
import com.wahyush04.core.helper.uriToFile
import kotlinx.android.synthetic.main.activity_register.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private var getFile: File? = null
    private var imageMultipart : MultipartBody.Part? = null
    private lateinit var currentPhotoPath: String
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
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
            showLoading(true)
            register()
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
            binding.emailedtlayout.error = getString(R.string.wrong_email_format)
        }else{
            binding.emailedtlayout.error = null
        }
    }

    private fun selectImageFrom() {
        val items = arrayOf(getString(R.string.camera), getString(R.string.galllery))
//        val builder = AlertDialog.Builder(this)

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.select_image))
            .setItems(items){ _, which ->
                when(which){
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
        val chooser = Intent.createChooser(intent, "Choose a Picture")
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

    private fun register(){

//        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
            imageMultipart = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )
        Log.d("photo","file : " + file.toString())
//        }

        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val confirmPassword = binding.edtPasswordConfirm.text.toString()
        val phone = binding.edtPhone.text.toString()
        val genderId = if (binding.rbMale.isChecked){ 0 }else{ 1 }

        Log.d("photo", "imageMultiPart : " + name+" "+ email +" "+password+" " + imageMultipart.toString() )

        if (password == confirmPassword){
            registerViewModel.register(name, email, password, phone, genderId, imageMultipart)
            registerViewModel.getRegisterResponse().observe(this){ data ->
                val status = data.success.status
                if (status == 201){
                    AlertDialog.Builder(this)
                        .setTitle("Register Success")
                        .setMessage("Register is successfully")
                        .setPositiveButton("Login") { _, _ ->
                            Toast.makeText(this,"Register Berhasil",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        .show()
                }
            }
            registerViewModel.registerError.observe(this){
                it.getContentIfNotHandled()?.let {
                    showLoading(false)
                    Toast.makeText(applicationContext, it.error.message, Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            binding.passwordedtlayout.error = "Password not match"
            binding.passwordconfirmedtlayout.error = "Password not match"
            Toast.makeText(applicationContext, "Password not match", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        showLoading(false)
    }
}