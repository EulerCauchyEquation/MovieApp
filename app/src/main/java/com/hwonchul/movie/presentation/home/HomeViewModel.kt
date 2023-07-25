package com.hwonchul.movie.presentation.home

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.Favorites
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.favorites.AddFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RefreshFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RemoveFavoritesUseCase
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
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val refreshFavoritesUseCase: RefreshFavoritesUseCase,
    private val addFavoritesUseCase: AddFavoritesUseCase,
    private val removeFavoritesUseCase: RemoveFavoritesUseCase,
) : BaseViewModel<HomeData, HomeState>(HomeData(), HomeState.Idle) {

    init {
        refresh()
        loadMovieList()
        loadMovieListAsPaged()
        loadUserAndRefreshFavorites()
    }

    private fun loadMovieList() {
        MovieListType.values().forEach { listType ->
            viewModelScope.launch {
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
        }
    }

    fun refresh() {
        viewModelScope.launch {
            MovieListType.values().forEach { type ->
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

    private suspend fun refreshMovieList(listType: MovieListType) {
        setState { HomeState.Loading }
        refreshMovieListUseCase(listType)
            .onSuccess { setState { HomeState.Idle } }
            .onFailure {
                setState { HomeState.Error(R.string.all_response_failed) }
                Timber.d(it)
            }
    }

    private fun loadUserAndRefreshFavorites() {
        viewModelScope.launch {
            setState { HomeState.Loading }
            getCurrentUserInfoUseCase().collectLatest { result ->
                result
                    .onSuccess { user ->
                        setData { copy(user = user) }
                        setState { HomeState.Idle }

                        // 가져온 사용자 ID로 찜 정보 동기화
                        refreshFavorites(user)
                    }
                    .onFailure { setState { HomeState.Error(R.string.user_info_get_failed) } }
            }
        }
    }

    private suspend fun refreshFavorites(user: User) {
        setState { HomeState.Loading }
        refreshFavoritesUseCase(user)
            .onSuccess { setState { HomeState.Idle } }
            .onFailure { HomeState.Error(R.string.user_refresh_failed) }
    }

    fun addFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorites = Favorites(movieId = movie.id, userId = uiData.value!!.user.uid)
            addFavoritesUseCase(favorites)
                .onSuccess { setState { HomeState.Idle } }
                .onFailure { setState { HomeState.Error(R.string.favorites_add_failure) } }
        }
    }

    fun removeFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorites = Favorites(movieId = movie.id, userId = uiData.value!!.user.uid)
            removeFavoritesUseCase(favorites)
                .onSuccess { setState { HomeState.Idle } }
                .onFailure { setState { HomeState.Error(R.string.favorites_remove_failure) } }
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