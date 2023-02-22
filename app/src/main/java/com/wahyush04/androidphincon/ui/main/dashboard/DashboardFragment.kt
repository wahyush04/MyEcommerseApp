package com.wahyush04.androidphincon.ui.main.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wahyush04.androidphincon.core.data.source.Resource
import com.wahyush04.androidphincon.databinding.FragmentDashboardBinding
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.androidphincon.ui.main.adapter.ProductFavoriteListAdapter
import com.wahyush04.core.Constant
import com.wahyush04.core.data.ErrorResponse
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.helper.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by viewModels()
    @Inject
    lateinit var preferences: PreferenceHelper
    private lateinit var adapter: ProductFavoriteListAdapter

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null
    private var febJob: Job? = null
    private var query: String? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ProductFavoriteListAdapter()
        adapter.notifyDataSetChanged()
        binding.rvProductList.setHasFixedSize(true)
        binding.rvProductList.adapter = adapter
        binding.febSort.visibility = View.GONE

        getData(null, null)
        binding.febSort.setOnClickListener {
            selectSorting()
        }

        binding.svSearch.doOnTextChanged { text, _, _, _ ->
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                text?.let {
                    delay(2000)
                    if (it.isEmpty()) {
                        getData(null, null)
                    } else {
                        query = text.toString()
                        getData(text.toString(), null)
                    }
                }
            }
        }

        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.febSort.hide()
                febJob?.cancel()
                febJob = coroutineScope.launch {
                    delay(2500)
                    binding.febSort.show()
                }
            }
        })

        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            getData(null, null)
            binding.svSearch.text = null
            binding.swipeRefreshLayout.isRefreshing = false
            searchJob?.cancel()
        }

        adapter.setOnItemClickCallback(object : ProductFavoriteListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
                val intent = Intent(getActivity(), DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }
        })

        return root
    }


    private fun getData(search: String?, sort: String?){
        val idUser = preferences.getPreference(Constant.ID)
        dashboardViewModel.getFavProduct(idUser!!.toInt(), search).observe(viewLifecycleOwner) { data ->
            when (data) {
                is Resource.Loading -> {
                    showEmpty(false)
                    binding.shimmerList.visibility = View.VISIBLE
                    binding.shimmerList.startShimmer()
                }
                is Resource.Success -> {
                    if (data.data?.success?.data?.isNotEmpty() == true) {
                        binding.shimmerList.visibility = View.GONE
                        binding.shimmerList.stopShimmer()
                        showEmpty(false)
                        adapter.setList(data.data.success.data)
                        binding.rvProductList.visibility = View.VISIBLE
                        when (sort) {
                            "From A to Z" -> {
                                data.data.success.data.sortedBy { it.name_product }.let { adapter.setList(it.toList()) }
                                binding.rvProductList.visibility = View.VISIBLE
                            }
                            "From Z to A" -> {
                                data.data.success.data.sortedByDescending { it.name_product }.let { adapter.setList(it.toList()) }
                                binding.rvProductList.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        binding.shimmerList.visibility = View.GONE
                        binding.shimmerList.stopShimmer()
                        showEmpty(true)
                        binding.rvProductList.visibility = View.GONE
                        binding.febSort.visibility = View.GONE
                    }
                }
                is Resource.Error -> {
                    showEmpty(false)
                    binding.shimmerList.visibility = View.GONE
                    binding.shimmerList.stopShimmer()
                    binding.febSort.visibility = View.GONE
                    try {
                        val err = data.errorBody?.string()?.let { it1 -> JSONObject(it1).toString() }
                        val gson = Gson()
                        val jsonObject = gson.fromJson(err, JsonObject::class.java)
                        val errorResponse = gson.fromJson(jsonObject, ErrorResponse::class.java)
                        val messageErr = errorResponse.error.message
                        AlertDialog.Builder(requireActivity()).setTitle("Oops, Something when wrong")
                            .setMessage(messageErr).setPositiveButton("Ok") { _, _ ->
                            }.show()
                    } catch (e: java.lang.Exception) {
                        val err = data.errorCode
                        Log.d("ErrorCode", "$err")
                    }
                }
                is Resource.Empty -> {
                    binding.apply {
                        showEmpty(true)
                    }
                    binding.rvProductList.visibility = View.GONE
                    binding.shimmerList.visibility = View.GONE
                    binding.shimmerList.stopShimmer()
                }
            }
        }
    }

    private fun showShimmer(state : Boolean){
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
                    "From A to Z" -> getData(query,"From A to Z")
                    "From Z to A" -> getData(query,"From Z to A")
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
        febJob?.cancel()
        searchJob?.cancel()
    }

    private fun showEmpty(state : Boolean){
        if (state){
            binding.emptyLayout.emptyLayout.visibility = View.VISIBLE
        } else {
            binding.emptyLayout.emptyLayout.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        getData(null, null)
    }

    override fun onPause() {
        super.onPause()
        febJob?.cancel()
        searchJob?.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        febJob?.cancel()
        searchJob?.cancel()
    }

    override fun onStop() {
        super.onStop()
        febJob?.cancel()
        searchJob?.cancel()
    }
//
//    override fun onStart() {
//        super.onStart()
//        febJob?.cancel()
//        searchJob?.cancel()
//    }

}