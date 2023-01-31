package com.wahyush04.androidphincon.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wahyush04.androidphincon.databinding.FragmentHomeBinding
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
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
    private var febJob: Job? = null

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


        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val isPhone = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK < Configuration.SCREENLAYOUT_SIZE_LARGE

        if (isPhone) {
            binding.rvProductList.layoutManager = LinearLayoutManager(context)
        }
        binding.rvProductList.setHasFixedSize(true)
        binding.rvProductList.adapter = adapter

        binding.febSort.setOnClickListener {
            selectSorting()
        }

        binding.febSort.hide()

//        getProduct(null, requireContext().applicationContext, sharedPreferences)
//        setData(null)

        binding.rvProductList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.febSort.hide()
//                if (dy >= 0) {
                    febJob?.cancel()
                    febJob = coroutineScope.launch {
                        delay(2500)
                        binding.febSort.show()
                    }
//                }
            }
        })

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
//                val view = view
//                val toDetail = HomeFragmentDirections.actionNavigationHomeToDetailProductActivity(data.id.toString())
//                view?.findNavController()?.navigate(toDetail)

                val intent = Intent(getActivity(), DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                Log.d("idHome", data.id.toString())
                startActivity(intent)
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
                    "From A to Z" -> setData("From A to Z")
                    "From Z to A" -> setData("From Z to A")
                }
            }
            .setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()

            }
            .show()
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
        febJob?.cancel()
        searchJob?.cancel()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        febJob?.cancel()
        searchJob?.cancel()
    }

    override fun onPause() {
        super.onPause()
        febJob?.cancel()
        searchJob?.cancel()
    }

    override fun onStop() {
        super.onStop()
        febJob?.cancel()
        searchJob?.cancel()
    }

    override fun onStart() {
        super.onStart()
        febJob?.cancel()
        searchJob?.cancel()
        val activity = requireActivity()
        val context = activity.applicationContext
        sharedPreferences = PreferenceHelper(context)
        coroutineScope.launch {
            homeViewModel.getProduct(null, requireContext().applicationContext , sharedPreferences)
        }
        setData(null)
    }

}