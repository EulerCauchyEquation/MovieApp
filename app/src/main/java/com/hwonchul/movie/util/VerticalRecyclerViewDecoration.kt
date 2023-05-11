package com.hwonchul.movie.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalRecyclerViewDecoration(
    private val outSideSpace: Int,            // 첫번째와 마지막 아이템의 바깥 공간
    private val divHeight: Int                   // 아이템과 아이템 사이 간격
) : ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // 첫번째 아이템
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = outSideSpace
        } else {
            outRect.top = divHeight
        }
    }
}