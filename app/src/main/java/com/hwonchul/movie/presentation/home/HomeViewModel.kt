package com.hwonchul.movie.presentation.home

import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase
import com.hwonchul.movie.presentation.home.HomeContract.HomeData
import com.hwonchul.movie.presentation.home.HomeContract.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val refreshMovieListUseCase: RefreshMovieListUseCase,
) : BaseViewModel<HomeData, HomeState>(HomeData(), HomeState.Idle) {

    init {
        loadData()
    }

    private fun loadData() {
        MovieListType.values().forEach { type ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    refreshMovieList(type)
                    loadMovieListByListType(type)
                }
            }
        }
    }

    suspend fun loadMovieListByListType(listType: MovieListType) {
        setState { HomeState.Loading }
        getMovieListUseCase(listType)
            .collectLatest { result ->
                result
                    .onSuccess {
                        setState { HomeState.Idle }
                        setData { setHomeData(listType, it) }
                    }
                    .onFailure {
                        setState { HomeState.Error(R.string.all_response_failed) }
                        Timber.d(it)
                    }
            }
    }

    private suspend fun refreshMovieList(listType: MovieListType) {
        setState { HomeState.Loading }
        refreshMovieListUseCase(listType)
            .onSuccess { setState { HomeState.Idle } }
            .onFailure {
                setState { HomeState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
    }
}

private fun HomeData.setHomeData(
    listType: MovieListType,
    it: List<Movie>
) = when (listType) {
    MovieListType.NowPlaying -> copy(popularMovieList = it)
    MovieListType.UpComing -> copy(upComingMovieList = it)
}