package com.team3205.junior.ui.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Декоратор для элементов recyclerView для отступов
 */
class OffsetItemDecorator(
    val left: Int,
    val right: Int,
    val top: Int,
    val bottom: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(left, top, right, bottom)
    }
}