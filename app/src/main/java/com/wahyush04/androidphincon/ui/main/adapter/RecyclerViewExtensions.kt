package com.wahyush04.androidphincon.ui.main.adapter

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.setDivider(
    dividerResId: Int? = null,
    orientation: Int = DividerItemDecoration.VERTICAL
) {
    val dividerItemDecoration = DividerItemDecoration(this.context, orientation)
    dividerResId?.let { resId ->
        this.context.getDrawable(resId)
            ?.let { divider ->
                dividerItemDecoration.setDrawable(divider)
            }
    }
    this.addItemDecoration(dividerItemDecoration)
}