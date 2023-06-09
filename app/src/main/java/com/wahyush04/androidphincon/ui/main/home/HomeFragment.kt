package com.wahyush04.androidphincon.ui.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.androidphincon.ui.adapter.ProductListAdapter
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.core.BaseFirebaseAnalytics
import com.wahyush04.core.data.source.remote.response.product.DataListProductPaging
import com.wahyush04.core.paging.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapter: ProductListAdapter
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null
    private val firebaseAnalytics = BaseFirebaseAnalytics()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProductListAdapter()

        binding.rvProductListHome.adapter = adapter

        getData(null)

        binding.svSearch.doOnTextChanged { text, start, before, count ->
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                text?.let {
                    delay(2000)
                    if (it.isEmpty()) {
                        getData(null)
                    } else {
                        //GA Slide 9 onSearch
                        firebaseAnalytics.onSearch("Home", text.toString())
                        getData(text.toString())
                    }
                }
            }
        }

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            getData(null)
            binding.svSearch.text = null
            binding.svSearch.clearFocus()
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
            binding.rvProductListHome.visibility = View.GONE
        }else{
            binding.rvProductListHome.visibility = View.VISIBLE
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
        //GA Slide 9 OnLoadScreen
        firebaseAnalytics.onLoadScreen("Home", this.javaClass.simpleName)
        searchJob?.cancel()
        getData(null)
    }

    override fun onPause() {
        super.onPause()
        searchJob?.cancel()
    }

    private fun getData(search : String?) {
        val adapter = ProductListAdapter()
        binding.rvProductListHome.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                showEmpty(true)
                showShimmer(false)
            } else {
                showEmpty(false)
            }
            showShimmer(loadState.refresh is LoadState.Loading)
        }

        homeViewModel.productListPaging(search).observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(object : ProductListAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DataListProductPaging) {
                    //GA Slide 9 onClickProduct
                    firebaseAnalytics.onClickProduct(
                        "Home",
                        data.nameProduct,
                        data.harga!!.toDouble(),
                        data.rate!!,
                        data.id!!
                    )
                    val intent =
                        Intent(requireActivity(), DetailProductActivity::class.java)
                    intent.putExtra("id", data.id)
                    startActivity(intent)
                }
            })
        }
        binding.svSearch.clearFocus()
    }

}