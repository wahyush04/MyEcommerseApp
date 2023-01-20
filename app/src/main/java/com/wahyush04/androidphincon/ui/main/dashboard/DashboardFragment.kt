package com.wahyush04.androidphincon.ui.main.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.api.ApiConfig
import com.wahyush04.androidphincon.databinding.FragmentDashboardBinding
import com.wahyush04.androidphincon.ui.main.adapter.ProductListAdapter
import com.wahyush04.androidphincon.ui.main.home.HomeViewModel
import com.wahyush04.core.Constant
import com.wahyush04.core.data.product.ProductResponse
import com.wahyush04.core.helper.PreferenceHelper
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

        val idUser : String? = sharedPreferences.getPreference(Constant.ID)

        binding.svSearch.doAfterTextChanged {
            val search : String?  = binding.svSearch.text.toString()
            getFavoriteProduct(search, idUser!!.toInt(), sharedPreferences)
        }

        dashboardViewModel.getFavoriteProductData().observe(viewLifecycleOwner){
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


        return root
    }

    fun getFavoriteProduct(search : String?, id : Int, preferences: PreferenceHelper){
        showShimmer(true)
        dashboardViewModel.getFavoriteProduct(search, id, requireContext().applicationContext, preferences)
    }

    fun showShimmer(state : Boolean){
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

}