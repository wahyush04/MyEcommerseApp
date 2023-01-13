package com.wahyush04.androidphincon.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.databinding.FragmentProfileBinding
import com.wahyush04.androidphincon.ui.changepassword.ChangePasswordActivity
import com.wahyush04.androidphincon.ui.login.LoginActivity
import com.wahyush04.core.helper.PreferenceHelper

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var sharedPreferences: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val pref : SharedPreferences = context.getSharedPreferences("loginData", Context.MODE_PRIVATE)
//        sharedPreferences = PreferenceHelper(requireActivity().)

//        val sharedPref = requireActivity().getPreferences("",Context.MODE_PRIVATE)

        val pref = context?.getSharedPreferences("loginData", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref!!.edit()

        binding.cvChangePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.cvLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to Logout")
                .setPositiveButton("Ya") { _, _ ->
                //Logout here
                    editor.clear().apply()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    Toast.makeText(
                        requireContext(),
                        "Log out Berhasil",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .setNegativeButton("No") { _, _ ->
                }
                .show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}