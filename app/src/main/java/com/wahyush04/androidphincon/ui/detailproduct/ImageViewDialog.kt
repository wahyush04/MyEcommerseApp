package com.wahyush04.androidphincon.ui.detailproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ImageViewDialogBinding

class ImageViewDialog(private val data: String) : DialogFragment() {

    private var _binding: ImageViewDialogBinding? = null
    private val binding get() =  _binding


    override fun getTheme(): Int {
        return R.style.NoBackgroundDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ImageViewDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            Glide.with(requireContext())
                .load(data)
                .into(imageViewDialog)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}