package com.hwonchul.movie.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * 수평 RecyclerView Decoration
 */
class HorizontalRecyclerViewDecoration(
    private val outSideSpace: Int,       // 첫번째와 마지막 아이템의 바깥 공간
    private val divWidth: Int              // 아이템과 아이템 사이 간격
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemsCount = parent.adapter!!.itemCount

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = outSideSpace
        } else if (parent.getChildAdapterPosition(view) == itemsCount - 1) {
            outRect.left = divWidth
            outRect.right = outSideSpace
        } else {
            outRect.left = divWidth
        }
    }
}