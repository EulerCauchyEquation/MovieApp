package com.hwonchul.movie.presentation.home

import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                refreshMovieList(MovieListType.NowPlaying)
                loadMovieListByListType(MovieListType.NowPlaying)
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
                        setData { copy(popularMovieList = it) }
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