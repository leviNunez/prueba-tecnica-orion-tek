package com.prueba.tinyshopapp.utils

import android.graphics.Rect
import android.view.View
import androidx.core.graphics.scaleMatrix
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSpacer(private val space: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = space
    }
}