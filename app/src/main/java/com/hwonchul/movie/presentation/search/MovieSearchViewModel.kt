package com.hwonchul.movie.presentation.search

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hwonchul.movie.R
import com.hwonchul.movie.base.view.BaseViewModel
import com.hwonchul.movie.domain.model.Favorites
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.User
import com.hwonchul.movie.domain.usecase.account.GetCurrentUserInfoUseCase
import com.hwonchul.movie.domain.usecase.favorites.AddFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RefreshFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RemoveFavoritesUseCase
import com.hwonchul.movie.domain.usecase.search.SearchMovieWithKeywordUseCase
import com.hwonchul.movie.presentation.search.MovieSearchContract.MovieSearchData
import com.hwonchul.movie.presentation.search.MovieSearchContract.MovieSearchState
import com.hwonchul.movie.util.NetworkStatusHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    val networkStatusHelper: NetworkStatusHelper,
    private val searchMovieWithKeywordUseCase: SearchMovieWithKeywordUseCase,
    private val getCurrentUserInfoUseCase: GetCurrentUserInfoUseCase,
    private val refreshFavoritesUseCase: RefreshFavoritesUseCase,
    private val addFavoritesUseCase: AddFavoritesUseCase,
    private val removeFavoritesUseCase: RemoveFavoritesUseCase,
) : BaseViewModel<MovieSearchData, MovieSearchState>(MovieSearchData(), MovieSearchState.Idle) {

    init {
        viewModelScope.launch {
            uiData.asFlow()
                .map { it.keyword }  // data중에 keyword 를 보고 처리
                .debounce(500)  // debounce 전략 사용
                .distinctUntilChanged()  // 이전 검색어와 동일하면 스킵
                .flatMapLatest { keyword ->
                    searchMovieWithKeywordUseCase(keyword)
                        .cachedIn(viewModelScope)
                }
                .collectLatest { pagingData ->
                    setData { copy(pagedMovieList = pagingData) }
                }
        }

        loadUserAndRefreshFavorites()
    }

    fun search(keyword: String) {
        setData { copy(keyword = keyword) }
    }

    private fun loadUserAndRefreshFavorites() {
        viewModelScope.launch {
            setState { MovieSearchState.Loading }
            getCurrentUserInfoUseCase().collectLatest { result ->
                result
                    .onSuccess { user ->
                        setData { copy(user = user) }
                        setState { MovieSearchState.Idle }

                        // 가져온 사용자 ID로 찜 정보 동기화
                        refreshFavorites(user)
                    }
                    .onFailure { setState { MovieSearchState.Error(R.string.user_info_get_failed) } }
            }
        }
    }

    private suspend fun refreshFavorites(user: User) {
        setState { MovieSearchState.Loading }
        refreshFavoritesUseCase(user)
            .onSuccess { setState { MovieSearchState.Idle } }
            .onFailure { MovieSearchState.Error(R.string.user_refresh_failed) }
    }

    fun addFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorites = Favorites(movieId = movie.id, userId = uiData.value!!.user.uid)
            addFavoritesUseCase(favorites)
                .onSuccess { setState { MovieSearchState.Idle } }
                .onFailure { setState { MovieSearchState.Error(R.string.favorites_add_failure) } }
        }
    }

    fun removeFavorites(movie: Movie) {
        viewModelScope.launch {
            val favorites = Favorites(movieId = movie.id, userId = uiData.value!!.user.uid)
            removeFavoritesUseCase(favorites)
                .onSuccess { setState { MovieSearchState.Idle } }
                .onFailure { setState { MovieSearchState.Error(R.string.favorites_remove_failure) } }
        }
    }
}