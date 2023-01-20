package com.wahyush04.androidphincon.ui.main.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.wahyush04.androidphincon.MainActivity
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.androidphincon.ui.main.adapter.ProductListAdapter
import com.wahyush04.androidphincon.ui.main.profile.ProfileViewModel
import com.wahyush04.core.Constant
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: ProductListAdapter

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProductListAdapter()
        adapter.notifyDataSetChanged()

        val activity = requireActivity()
        val context = activity.applicationContext
        sharedPreferences = PreferenceHelper(context)

        binding.rvProductList.layoutManager = LinearLayoutManager(context)
        binding.rvProductList.setHasFixedSize(true)
        binding.rvProductList.adapter = adapter

        binding.svSearch.doOnTextChanged { text, start, before, count ->
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                text?.let {
                    delay(2000)
                    if (it.isEmpty()) {
                        getProduct(null, requireContext().applicationContext, sharedPreferences)
                    } else {
                        getProduct(text.toString(), requireContext().applicationContext, sharedPreferences)
                    }
                }
            }

        }

        setData()
        adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                Toast.makeText(requireContext().applicationContext, data.name_product, Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    fun setData(){
        homeViewModel.getProductData().observe(viewLifecycleOwner){
            if (it != null) {
                adapter.setList(it)
                binding.rvProductList.visibility = View.VISIBLE
            }

            if (it != null) {
                if (it.isEmpty()){
                    Toast.makeText(requireContext().applicationContext, "No Data", Toast.LENGTH_SHORT).show()
                }
            }
            showShimmer(false)
        }
    }

    private fun getProduct(search : String?, context : Context, preferences : PreferenceHelper){
        showShimmer(true)
        homeViewModel.getProduct(search, context , preferences)

    }

    private fun showShimmer(state : Boolean){
        if (state){
            binding.shimmerList.visibility = View.VISIBLE
            binding.shimmerList.startShimmer()
        }else{
            binding.shimmerList.visibility = View.GONE
            binding.shimmerList.stopShimmer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val activity = requireActivity()
        val context = activity.applicationContext
        sharedPreferences = PreferenceHelper(context)
        homeViewModel.getProduct(null, requireContext().applicationContext , sharedPreferences)
        setData()
    }
}