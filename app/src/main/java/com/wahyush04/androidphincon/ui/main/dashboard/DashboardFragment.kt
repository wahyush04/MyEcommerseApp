package com.wahyush04.androidphincon.ui.main.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.wahyush04.androidphincon.databinding.FragmentDashboardBinding
import com.wahyush04.androidphincon.ui.detailproduct.DetailProductActivity
import com.wahyush04.androidphincon.ui.main.adapter.ProductFavoriteListAdapter
import com.wahyush04.core.Constant
import com.wahyush04.core.data.product.DataListProduct
import com.wahyush04.core.helper.PreferenceHelper
import kotlinx.coroutines.*


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var adapter: ProductFavoriteListAdapter

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

        adapter = ProductFavoriteListAdapter()
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

        binding.svSearch.doOnTextChanged { text, _, _, _ ->
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

        adapter.setOnItemClickCallback(object : ProductFavoriteListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataListProduct) {
//                val view = view
//                val toDetail = DashboardFragmentDirections.actionNavigationDashboardToDetailProductActivity(data.id.toString())
//                view?.findNavController()?.navigate(toDetail)

                val intent = Intent(getActivity(), DetailProductActivity::class.java)
                intent.putExtra("id", data.id)
                startActivity(intent)
            }
        })

        return root
    }

    private fun getFavoriteProduct(search : String?, id : Int, preferences: PreferenceHelper){
        showShimmer(true)
        dashboardViewModel.getFavoriteProduct(search, id, requireContext().applicationContext, preferences)
    }

    private fun setData(sort : String?){
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

    override fun onDetach() {
        super.onDetach()
        febJob?.cancel()
        searchJob?.cancel()
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
        dashboardViewModel.getFavoriteProduct(null, sharedPreferences.getPreference(Constant.ID)!!.toInt(),requireContext().applicationContext, sharedPreferences)
        setData(null)
    }

}