package com.wahyush04.androidphincon.ui.main.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.customview.CustomSpinnerAdapter
import com.wahyush04.androidphincon.databinding.FragmentProfileBinding
import com.wahyush04.androidphincon.ui.changepassword.ChangePasswordActivity
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.reduceFileImage
import com.wahyush04.core.helper.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var getFile: File? = null
    private var imageMultipart : MultipartBody.Part? = null
    private lateinit var currentPhotoPath: String
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var profileViewModel: ProfileViewModel
    private var isUserAction = false
    val arrLanguage = arrayOf("IN","EN","ID")
    val arrFlag = intArrayOf(R.drawable.united_nations, R.drawable.united_states, R.drawable.indonesia)

    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        sharedPreferences = PreferenceHelper(requireContext())

        val image : String? = sharedPreferences.getToken(Constant.IMAGE)
        val name : String? = sharedPreferences.getToken(Constant.NAME)
        val email : String? = sharedPreferences.getToken(Constant.EMAIL)
        val localeID : String? = sharedPreferences.getToken(Constant.LOCALE)
        binding.tvName.text = name
        binding.tvEmail.text = email

        Glide.with(this)
            .load(image)
            .into(binding.ivPhotoProfile)

        binding.cvChangePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnChangeImage.setOnClickListener {
            selectImageFrom()
        }

        binding.cvLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to Logout")
                .setPositiveButton("Ya") { _, _ ->
                    logout()
                }
                .setNegativeButton("No") { _, _ ->
                }
                .show()
        }


        val customSpinnerAdapter = CustomSpinnerAdapter(requireContext(), arrFlag, arrLanguage)
        binding.languageSpinner.adapter = customSpinnerAdapter

        val spinner = binding.languageSpinner

        spinner.setOnTouchListener { view, motionEvent ->
            isUserAction = true
            false
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUserAction) {
                    if (position == 2) {
                        setLocate("in")
                        Log.d("language", position.toString())
                        sharedPreferences.putLocale(position.toString())
                        activity!!.recreate()
                    } else if (position == 1){
                        setLocate("en")
                        Log.d("language", position.toString())
                        sharedPreferences.putLocale(position.toString())
                        activity!!.recreate()
                    }
                } else {
                    isUserAction = false
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (localeID != null) {
            binding.languageSpinner.setSelection(localeID.toInt())
        } else {
            binding.languageSpinner.setSelection(1)
        }

        return binding.root
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, requireActivity())

            getFile = myFile

            changeImage()

        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        com.wahyush04.core.helper.createTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
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
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            changeImage()
        }
    }

    private fun logout(){
        sharedPreferences.clear()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Toast.makeText(
            requireContext(),
            "Log out Berhasil",
            Toast.LENGTH_LONG
        ).show()
        activity?.finish()
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

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun selectImageFrom() {
        val items = arrayOf(getString(R.string.camera), getString(R.string.galllery))
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_image))
            .setItems(items){ _, which ->
                when(which){
                    0 -> startTakePhoto()
                    1 -> startGallery()
                }
            }
            .show()
    }

    private fun setLocate(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeImage(){
        val file = reduceFileImage(getFile as File)
        val requestImageFIle =  file.asRequestBody("image/jpg".toMediaType())
        imageMultipart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFIle
        )

        val id = sharedPreferences.getToken(Constant.ID)!!

        profileViewModel.changeImage( id, imageMultipart!!, sharedPreferences, requireContext().applicationContext)
        profileViewModel.getChangeImageResponse().observe(this){ data ->
            val status = data.success.status
            if (status == 200){
                kotlin.run {
                    sharedPreferences.changeImage(data.success.path)
                }
                Log.d("newimage : ", "preference"+sharedPreferences.getPreference(Constant.IMAGE).toString())
                Log.d("newimage : ", "newimage"+data.success.path)

                Glide.with(this)
                    .load(data.success.path)
                    .into(binding.ivPhotoProfile)
            }
        }
    }


}