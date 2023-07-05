package com.hwonchul.movie.presentation.home

import androidx.paging.PagingData
import com.hwonchul.movie.base.view.UiData
import com.hwonchul.movie.base.view.UiState
import com.hwonchul.movie.domain.model.Movie

class HomeContract {

    data class HomeData(
        val popularMovieList: List<Movie> = listOf(),
        val upComingMovieList: List<Movie> = listOf(),
        val pagedPopularMovieList: PagingData<Movie> = PagingData.empty(),
        val pagedUpComingMovieList: PagingData<Movie> = PagingData.empty(),
    ) : UiData

    sealed class HomeState : UiState {
        object Loading : HomeState()
        object Idle : HomeState()
        data class Error(val message: Int) : HomeState()
    }
}