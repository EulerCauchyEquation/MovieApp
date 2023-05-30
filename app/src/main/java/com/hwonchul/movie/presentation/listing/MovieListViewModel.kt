package com.hwonchul.movie.presentation.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase
import com.hwonchul.movie.presentation.listing.MovieListContract.MovieListData
import com.hwonchul.movie.presentation.listing.MovieListContract.MovieListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val refreshMovieListUseCase: RefreshMovieListUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieListData, MovieListState>(MovieListData(), MovieListState.Idle) {

    init {
        loadData()
    }

    private fun loadData() {
        val listType = listType

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                refreshMovieList(listType)
                loadMovieListByListType(listType)
           }
        }
    }

    private val listType: MovieListType
        get() {
            val type = savedStateHandle.get<MovieListType>(KEY_LIST_TYPE)
            Timber.d("listType : %s", type)
            return type ?: MovieListType.NowPlaying
        }

    suspend fun loadMovieListByListType(listType: MovieListType) {
        setState { MovieListState.Loading }
        getMovieListUseCase(listType)
            .collectLatest { result ->
                result
                    .onSuccess {
                        setState { MovieListState.Idle }
                        setData { copy(movieList = it) }
                    }
                    .onFailure {
                        setState { MovieListState.Error(R.string.all_response_failed) }
                        Timber.d(it)
                    }
            }
    }

    private suspend fun refreshMovieList(listType: MovieListType) {
        setState { MovieListState.Loading }
        refreshMovieListUseCase(listType)
            .onSuccess { setState { MovieListState.Idle } }
            .onFailure {
                setState { MovieListState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
        savedStateHandle[KEY_LIST_TYPE] = listType
    }

    companion object {
        private const val KEY_LIST_TYPE = "MovieListType"
    }
}