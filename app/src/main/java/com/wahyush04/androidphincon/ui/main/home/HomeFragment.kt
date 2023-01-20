package com.wahyush04.androidphincon.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.androidphincon.ui.main.adapter.ProductListAdapter
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

        binding.febSort.setOnClickListener {
            selectSorting()
        }

        setData(null)

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

        adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                Toast.makeText(requireContext().applicationContext, data.name_product, Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }


    private fun setData(sort : String?){
        showShimmer(true)
        homeViewModel.getProductData().observe(viewLifecycleOwner){ data ->
            if (data != null) {
                if (sort == "From A to Z"){
                    adapter.setList(data.sortedBy { it.name_product }.toList())
                    binding.rvProductList.visibility = View.VISIBLE
                } else if (sort == "From Z to A") {
                    adapter.setList(data.sortedByDescending { it.name_product }.toList())
                    binding.rvProductList.visibility = View.VISIBLE
                } else{
                    adapter.setList(data)
                    binding.rvProductList.visibility = View.VISIBLE
                }
                showEmpty(false)
            } else {
                showEmpty(true)
                Toast.makeText(requireContext().applicationContext, "No Data", Toast.LENGTH_SHORT).show()
            }

            if (data != null) {
                if (data.isEmpty()){
                    showEmpty(true)
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

    fun showShimmer(state : Boolean){
        if (state){
            binding.rvProductList.visibility = View.GONE
            binding.shimmerList.visibility = View.VISIBLE
            binding.shimmerList.startShimmer()
        }else{
            binding.rvProductList.visibility = View.VISIBLE
            binding.shimmerList.visibility = View.GONE
            binding.shimmerList.stopShimmer()
        }
    }

    private fun selectSorting() {
        val items = arrayOf("From A to Z", "From Z to A")
        var selectedOption = ""
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Sort By")
            .setSingleChoiceItems(items, -1){_, which ->
                selectedOption = items[which]
            }
            .setPositiveButton("OK"){_,_ ->
                when (selectedOption){
                    "From A to Z" -> setData("From A to Z")
                    "From Z to A" -> setData("From Z to A")
                }

            }
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()

            }
            .show()
    }

    fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
        }
    }

    private fun sortAsc(){
        Toast.makeText(requireContext().applicationContext, "Sort Ascending", Toast.LENGTH_SHORT).show()
    }

    private fun sortDesc(){

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
        setData(null)
    }
}