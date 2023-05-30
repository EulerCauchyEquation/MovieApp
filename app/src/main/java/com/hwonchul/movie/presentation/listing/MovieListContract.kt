package com.hwonchul.movie.presentation.listing

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.Movie

class MovieListContract {

    data class MovieListData(
        val movieList: List<Movie> = listOf()
    ) : UiData

    sealed class MovieListState : UiState {
        object Loading : MovieListState()
        object Idle : MovieListState()
        data class Error(val message: Int) : MovieListState()
    }
}