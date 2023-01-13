package com.wahyush04.androidphincon.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.wahyush04.androidphincon.MainActivity
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.core.Constant
import com.wahyush04.core.helper.PreferenceHelper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = com.wahyush04.androidphincon.databinding.FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val context = requireContext()
        val activity = requireActivity()
        val context = activity.applicationContext

        sharedPreferences = PreferenceHelper(context)
        val token : String? = sharedPreferences.getToken(Constant.PHONE)
        Log.d("PrefName", token.toString())

        return root
    }

    private fun logout(){
        sharedPreferences.clear()
        startActivity(Intent(activity, MainActivity::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}