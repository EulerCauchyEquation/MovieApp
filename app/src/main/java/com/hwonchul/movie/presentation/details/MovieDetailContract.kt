package com.hwonchul.movie.presentation.details

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.Video

class MovieDetailContract {

    data class MovieDetailData(
        val movieDetail: MovieDetail = MovieDetail.EMPTY,
        val videos: List<Video> = listOf(),
        val images: List<Image> = listOf(),
    ) : UiData

    sealed class MovieDetailState : UiState {
        object Loading : MovieDetailState()
        object Idle : MovieDetailState()
        data class Error(val message: Int) : MovieDetailState()
    }
}