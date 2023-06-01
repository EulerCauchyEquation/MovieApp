package com.hwonchul.movie.presentation.home

import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.Movie

class HomeContract {

    data class HomeData(
        val popularMovieList: List<Movie> = listOf()
    ) : UiData

    sealed class HomeState : UiState {
        object Loading : HomeState()
        object Idle : HomeState()
        data class Error(val message: Int) : HomeState()
    }
}