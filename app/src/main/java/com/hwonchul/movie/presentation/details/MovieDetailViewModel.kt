package com.hwonchul.movie.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.usecase.details.GetMovieUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshImageListUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshMovieUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshVideoListUseCase
import com.hwonchul.movie.presentation.details.MovieDetailContract.MovieDetailData
import com.hwonchul.movie.presentation.details.MovieDetailContract.MovieDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMovieUseCase: GetMovieUseCase,
    private val refreshMovieUseCase: RefreshMovieUseCase,
    private val refreshImageListUseCase: RefreshImageListUseCase,
    private val refreshVideoListUseCase: RefreshVideoListUseCase
) : BaseViewModel<MovieDetailData, MovieDetailState>(MovieDetailData(), MovieDetailState.Idle) {

    init {
        loadData()
    }

    fun loadData() {
        val movie = savedStateHandle.get<Movie>(KEY_MOVIE)
        Timber.d("movie : %s", movie)

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // refresh 모두 마치고 load
                refreshMovie(movie!!.id)
                refreshPhotos(movie.id)
                refreshVideos(movie.id)
                loadMovie(movie.id)
            }
        }
    }

    private suspend fun loadMovie(movieId: Int) {
        setState { MovieDetailState.Loading }
        getMovieUseCase(movieId)
            .collectLatest { result ->
                result
                    .onSuccess {
                        setState { MovieDetailState.Idle }
                        setData { copy(movieDetail = it) }
                    }
                    .onFailure {
                        setState { MovieDetailState.Error(R.string.all_response_failed) }
                        Timber.d(it)
                    }
            }
    }

    private suspend fun refreshMovie(movieId: Int) {
        setState { MovieDetailState.Loading }
        refreshMovieUseCase(movieId)
            .onSuccess { setState { MovieDetailState.Idle } }
            .onFailure {
                setState { MovieDetailState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
    }

    private suspend fun refreshPhotos(movieId: Int) {
        setState { MovieDetailState.Loading }
        refreshImageListUseCase(movieId)
            .onSuccess { setState { MovieDetailState.Idle } }
            .onFailure {
                setState { MovieDetailState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
    }

    private suspend fun refreshVideos(movieId: Int) {
        setState { MovieDetailState.Loading }
        refreshVideoListUseCase(movieId)
            .onSuccess { setState { MovieDetailState.Idle } }
            .onFailure {
                setState { MovieDetailState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
    }

    companion object {
        private const val KEY_MOVIE = "movie"
    }
}