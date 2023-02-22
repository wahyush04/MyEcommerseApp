package com.wahyush04.androidphincon.customview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.wahyush04.androidphincon.*

class CustomSpinnerAdapter(internal var context: Context, private var images: IntArray, private var languages: Array<String>):  BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return images.size
    }
    override fun getItem(p0: Int): Any? {
        return null
    }
    override fun getItemId(p0: Int): Long {
        return 0
    }
    @SuppressLint("ViewHolder")
    override fun getView(i: Int, p1: View?, p2: ViewGroup?): View {
        val view = inflater.inflate(R.layout.language_spinner,null)
        val icon = view.findViewById<View>(R.id.iv_language) as ImageView?
        val names = view.findViewById<View>(R.id.language) as TextView?
        icon!!.setImageResource(images[i])
        names!!.text = languages[i]
        view.setPadding(0, view.paddingTop, 0, view.paddingBottom)
        return view
    }
}