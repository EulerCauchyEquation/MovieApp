package com.hwonchul.movie.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 수평 RecyclerView Decoration
 */
public class HorizontalRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    // 첫번째와 마지막 아이템의 바깥 공간
    private final int outSideSpace;
    // 아이템과 아이템 사이 간격
    private final int divWidth;

    public HorizontalRecyclerViewDecoration(int outSideSpace, int divWidth) {
        this.outSideSpace = outSideSpace;
        this.divWidth = divWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int itemsCount = parent.getAdapter().getItemCount();
        // 첫번째 아이템
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = outSideSpace;
        }
        // 마지막 아이템
        else if (parent.getChildAdapterPosition(view) == itemsCount - 1) {
            outRect.left = divWidth;
            outRect.right = outSideSpace;
        }
        // 그 외
        else {
            outRect.left = divWidth;
        }
    }
}
