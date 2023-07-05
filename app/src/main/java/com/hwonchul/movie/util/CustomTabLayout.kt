package com.hwonchul.movie.util

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.hwonchul.movie.R

class CustomTabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TabLayout(context, attrs, defStyleAttr) {

    private var tabItemMargin: Int = 0

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout, 0, 0)
            .apply {
                try {
                    // set tab margin
                    tabItemMargin =
                        getDimensionPixelSize(R.styleable.CustomTabLayout_tabItemMargin, 0)
                } finally {
                    recycle()
                }
            }
    }

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        val tabStrip = getChildAt(0) as ViewGroup
        for (i in 0 until tabStrip.childCount) {
            val tabView = tabStrip.getChildAt(i)
            val params = tabView.layoutParams as MarginLayoutParams
            if (i == 0) {
                // 가장 앞 tabItem
                params.marginStart = tabItemMargin
            } else if (i == tabStrip.childCount - 1) {
                // 가장자리에 있는 tabItem
                params.marginStart = tabItemMargin / 2
                params.marginEnd = tabItemMargin
            } else {
                // 중앙에 있는 tabItem
                params.marginStart = tabItemMargin / 2
            }
            tabView.requestLayout()
        }
    }
}