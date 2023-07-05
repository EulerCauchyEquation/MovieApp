package com.hwonchul.movie.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.usecase.listing.GetMovieListAsPagedUseCase
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase
import com.hwonchul.movie.presentation.home.HomeContract.HomeData
import com.hwonchul.movie.presentation.home.HomeContract.HomeState
import com.hwonchul.movie.util.NetworkStatusHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val networkStatusHelper: NetworkStatusHelper,
    private val getMovieListUseCase: GetMovieListUseCase,
    private val getMovieListAsPagedUseCase: GetMovieListAsPagedUseCase,
    private val refreshMovieListUseCase: RefreshMovieListUseCase,
) : BaseViewModel<HomeData, HomeState>(HomeData(), HomeState.Idle) {

    init {
        loadData()
    }

    fun loadData() {
        refresh()
        loadMovieList()
        loadMovieListAsPaged()
    }

    fun loadMovieList() {
        MovieListType.values().forEach { type ->
            viewModelScope.launch {
                loadMovieListByListType(type)
            }
        }
    }

    fun refresh() {
        MovieListType.values().forEach { type ->
            viewModelScope.launch {
                refreshMovieList(type)
            }
        }
    }

    private fun loadMovieListAsPaged() {
        MovieListType.values().forEach { type ->
            viewModelScope.launch {
                getMovieListAsPagedUseCase(type).cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        setData {
                            when (type) {
                                MovieListType.NowPlaying -> copy(pagedPopularMovieList = pagingData)
                                MovieListType.UpComing -> copy(pagedUpComingMovieList = pagingData)
                            }
                        }
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