package com.wahyush04.androidphincon.ui.main.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import com.wahyush04.androidphincon.R
import com.wahyush04.androidphincon.databinding.ListProductBinding
import com.wahyush04.androidphincon.databinding.ViewBookDetailsComponentBinding
import com.wahyush04.core.data.product.DataListProduct

class OtherProductComponentView : ConstraintLayout {

    private lateinit var binding: ViewBookDetailsComponentBinding
    private lateinit var bookItemBinding: ListProductBinding
    private lateinit var adapter: BookAdapter

    var books: List<DataListProduct> = emptyList()
        set(value) {
            field = value
            onItemsUpdated()
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        binding = ViewBookDetailsComponentBinding.inflate(LayoutInflater.from(context), this, true)
        adapter = BookAdapter(context)
        binding.bookDetailsList.adapter = adapter
    }

    private fun onItemsUpdated() {
        adapter.notifyDataSetChanged()
//        binding.bookDetailsList.requestLayoutForChangedDataset()
    }

    inner class BookAdapter(private val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val book: DataListProduct = books[position]
            var view: View? = convertView

            if (view == null) {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.list_product, parent, false)
                bookItemBinding = ListProductBinding.bind(view)
                view.tag = bookItemBinding
            } else {
                bookItemBinding = view.tag as ListProductBinding
            }

            bookItemBinding.apply {
                tvProductName.text = book.name_product
                tvPrice.text = book.harga
                tvDate.text = book.date
                ratingBar.rating = book.rate.toFloat()
            }

            return bookItemBinding.root
        }

        override fun getItem(position: Int): Any {
            return books[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return books.size
        }

        override fun isEnabled(position: Int): Boolean {
            return false
        }
    }
}