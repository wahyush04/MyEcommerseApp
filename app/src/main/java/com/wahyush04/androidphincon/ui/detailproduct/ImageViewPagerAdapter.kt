package com.wahyush04.androidphincon.ui.detailproduct

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.wahyush04.core.data.detailproduct.ImageProductItem
import com.wahyush04.core.database.ProductEntity

class ImageViwPagerAdapter(
    private val images: List<ImageProductItem?>?,
    ) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        Glide.with(container.context)
            .load(images?.get(position)?.image_product)
            .centerCrop()
            .into(imageView)
        container.addView(imageView)
        return imageView
    }


    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getCount(): Int {
        return images!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
}