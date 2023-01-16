package com.wahyush04.androidphincon.ui.main.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
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
import com.wahyush04.androidphincon.MainActivity
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.customview.CustomSpinnerAdapter
import com.wahyush04.androidphincon.databinding.FragmentProfileBinding
import com.wahyush04.androidphincon.ui.changepassword.ChangePasswordActivity
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper
import com.wahyush04.core.helper.uriToFile
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MultipartBody
import java.io.File
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var getFile: File? = null
    private var imageMultipart : MultipartBody.Part? = null
    private lateinit var currentPhotoPath: String
    private lateinit var sharedPreferences: PreferenceHelper
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    val arrLanguage = arrayOf("EN","ID")
    val arrFlag = intArrayOf(R.drawable.united_states, R.drawable.indonesia)


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        sharedPreferences = PreferenceHelper(requireContext())

        val image : String? = sharedPreferences.getToken(Constant.IMAGE)
        Log.d("IMAGE", "image : " + image.toString())

        Glide.with(this)
            .load(image)
            .into(binding.ivPhotoProfile)

//        val pref = context?.getSharedPreferences("loginData", Context.MODE_PRIVATE)
//        val editor : SharedPreferences.Editor = pref!!.edit()

        binding.cvChangePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddImage.setOnClickListener {
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


        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    setLocate("en")
//                    activity!!.recreate()
                    Log.d("language", "setlocale")

                } else if (position == 1) {
                    setLocate("in")
//                    activity!!.recreate()
                    Log.d("language", "setlocale")
                }
                Toast.makeText(requireContext(),"you select: $position \n${arrLanguage[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        val customSpinnerAdapter = CustomSpinnerAdapter(requireContext(), arrFlag, arrLanguage)
        binding.languageSpinner.adapter = customSpinnerAdapter
        return binding.root
    }

    private fun logout(){
        sharedPreferences.clear()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        Toast.makeText(
            requireContext(),
            "Log out Berhasil",
            Toast.LENGTH_LONG
        ).show()
        activity?.finish()
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


            val result = BitmapFactory.decodeFile(myFile.path)

            val rotatedImage = rotateBitmap(result)

            binding.ivPhotoProfile.setImageBitmap(rotatedImage)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, requireContext())

            getFile = myFile

            binding.ivPhotoProfile.setImageURI(selectedImg)
        }
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectImageFrom() {
        val items = arrayOf(getString(R.string.camera), getString(R.string.galllery))
//        val builder = AlertDialog.Builder(this)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_image))
            .setItems(items){ _, which ->
                when(which){
                    0 -> startTakePhoto()
                    1 -> startGallery()
                }
            }
            .show()
//        builder.setTitle(getString(R.string.select_image))
//        builder.setItems(items) { _, which ->
//            if (items[which] == "Camera"){
//                startTakePhoto()
//            }else{
//                startGallery()
//            }
//            Toast.makeText(applicationContext, items[which], Toast.LENGTH_SHORT).show()
//        }

//        val dialog = builder.create()
//        dialog.show()
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setLanguage()
//    }

    private fun setLocate(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
    }

    fun setLanguage(){
        Log.d("language", "setLanguage")

//        binding.apply {
//            language_spinner.post(Runnable {
//
//            })
//            val customSpinnerAdapter = CustomSpinnerAdapter(requireContext(), arrFlag, arrLanguage)
//            language_spinner.adapter = customSpinnerAdapter
//        }


    }
}