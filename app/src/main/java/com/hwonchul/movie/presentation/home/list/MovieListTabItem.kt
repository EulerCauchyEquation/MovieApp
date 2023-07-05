package com.hwonchul.movie.presentation.home.list

import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.MovieListType

enum class MovieListTabItem(
    val titleResId: Int,
    val type: MovieListType,
) {
    NOW_PLAYING(R.string.item_popular, MovieListType.NowPlaying),
    UPCOMING(R.string.item_upcoming, MovieListType.UpComing);

    companion object {

        fun getTabItemByType(listType: MovieListType) = values().first { it.type == listType }
    }
}