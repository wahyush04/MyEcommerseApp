package com.wahyush04.androidphincon.ui.main.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.databinding.FragmentDashboardBinding
import com.wahyush04.androidphincon.ui.main.adapter.ProductListAdapter
import com.wahyush04.androidphincon.ui.main.home.HomeViewModel
import com.wahyush04.core.Constant
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var adapter: ProductListAdapter

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null
    private var febJob: Job? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProductListAdapter()
        adapter.notifyDataSetChanged()

        val activity = requireActivity()
        val context = activity.applicationContext
        sharedPreferences = PreferenceHelper(context)

        binding.rvProductList.layoutManager = LinearLayoutManager(context)
        binding.rvProductList.setHasFixedSize(true)
        binding.rvProductList.adapter = adapter

        val id = sharedPreferences.getPreference(Constant.ID)

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
                        getFavoriteProduct(null, id!!.toInt(), sharedPreferences)
                    } else {
                        getFavoriteProduct(text.toString(), id!!.toInt(), sharedPreferences)
                    }
                }
            }
        }

        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.febSort.hide()
                if (dy >= 0) {
                    febJob?.cancel()
                    febJob = coroutineScope.launch {
                        delay(3000)
                        binding.febSort.show()
                    }
                } else if (dy <= 0) {
                    febJob?.cancel()
                    febJob = coroutineScope.launch {
                        delay(3000)
                        binding.febSort.show()
                    }
                }
            }
        })

        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.febSort.hide()
                if (dy >= 0) {
                    febJob?.cancel()
                    febJob = coroutineScope.launch {
                        delay(3000)
                        binding.febSort.show()
                    }
                } else if (dy <= 0) {
                    febJob?.cancel()
                    febJob = coroutineScope.launch {
                        delay(3000)
                        binding.febSort.show()
                    }
                }
            }
        })

        adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                Toast.makeText(requireContext().applicationContext, data.name_product, Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    fun getFavoriteProduct(search : String?, id : Int, preferences: PreferenceHelper){
        showShimmer(true)
        dashboardViewModel.getFavoriteProduct(search, id, requireContext().applicationContext, preferences)
    }

    fun setData(sort : String?){
        showShimmer(true)
        dashboardViewModel.getFavoriteProductData().observe(viewLifecycleOwner){ data ->
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
            }else{
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        val activity = requireActivity()
        val context = activity.applicationContext
        sharedPreferences = PreferenceHelper(context)
        dashboardViewModel.getFavoriteProduct(null, sharedPreferences.getPreference(Constant.ID)!!.toInt(),requireContext().applicationContext, sharedPreferences)
        setData(null)
    }

}