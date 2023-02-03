package com.wahyush04.androidphincon.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.androidphincon.paging.LoadingStateAdapter
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.androidphincon.ui.main.adapter.ProductListAdapter
import com.wahyush04.core.data.product.DataListProductPaging
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
//    private lateinit var adapter: ProductListAdapter

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProductListAdapter()


        sharedPreferences = PreferenceHelper(requireContext())
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val isPhone = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE

        if (isPhone) {
            binding.rvProductList.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvProductList.adapter = adapter

        val factory = ViewModelFactory(requireContext().applicationContext, sharedPreferences)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        getData(null)

        binding.svSearch.doOnTextChanged { text, start, before, count ->
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                text?.let {
                    delay(2000)
                    if (it.isEmpty()) {
                        getData(null)
                    } else {
                        getData(text.toString())
                    }
                }
            }
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            getData(null)
            searchJob?.cancel()
            swipeRefreshLayout.isRefreshing = false
        }

        adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProductPaging) {
                val intent = Intent(activity, DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                Log.d("idHome", data.id.toString())
                startActivity(intent)
            }
        })

        return root
    }


    private fun showShimmer(state : Boolean){
        if (state){
            binding.shimmerList.startShimmer()
            binding.shimmerList.visibility = View.VISIBLE
            binding.rvProductList.visibility = View.GONE
        }else{
            binding.rvProductList.visibility = View.VISIBLE
            binding.shimmerList.visibility = View.GONE
            binding.shimmerList.stopShimmer()
        }
    }

    private fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
        }
    }

    override fun onDetach() {
        super.onDetach()
        searchJob?.cancel()
    }

    override fun onResume() {
        super.onResume()
        searchJob?.cancel()
        getData(null)
    }


    override fun onPause() {
        super.onPause()
        searchJob?.cancel()
    }

    private fun getData(search : String?) {
        val adapter = ProductListAdapter()
        binding.rvProductList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener { loadState ->
            showShimmer(loadState.refresh is LoadState.Loading)
        }

        homeViewModel.productListPaging(search).observe(viewLifecycleOwner) {
            Log.d("showEmpty", it.toString())
            if (it == null){
                showEmpty(true)
            }else{
                adapter.submitData(lifecycle, it)
                adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: DataListProductPaging) {
                        val intent = Intent(requireActivity(), DetailProductActivity::class.java)
                        intent.putExtra("id", data.id)
                        startActivity(intent)
                    }
                })
            }
        }

    }

    override fun onStart() {
        super.onStart()
    }



}