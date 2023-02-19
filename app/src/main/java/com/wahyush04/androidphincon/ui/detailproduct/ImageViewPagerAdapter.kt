package com.wahyush04.androidphincon.ui.detailproduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.wahyush04.androidphincon.R
import com.wahyush04.core.data.detailproduct.ImageProductItem

class ImageViewPagerAdapter(private val images: List<ImageProductItem>?, private val listener: OnPageClickListener) : PagerAdapter() {

    @SuppressLint("MissingInflatedId")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(container.context)
        val view = layoutInflater.inflate(R.layout.view_pager_layout, container, false)
        val imageView = view.findViewById<ImageView>(R.id.iv_detail_product)
        val titleView = view.findViewById<TextView>(R.id.tv_detail_image)
        Glide.with(container.context)
            .load(images?.get(position)?.image_product)
            .centerCrop()
            .into(imageView)
        titleView.text = images?.get(position)?.title_product ?: "Foto"

        view.setOnClickListener {
            images?.get(position)?.image_product?.let { it1 -> listener.onClick(it1) }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int {
        return images?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    interface OnPageClickListener {
        fun onClick(image: String)
    }

}
