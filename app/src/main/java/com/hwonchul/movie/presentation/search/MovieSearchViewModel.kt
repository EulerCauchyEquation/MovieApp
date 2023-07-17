package com.hwonchul.movie.presentation.search

import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hwonchul.movie.base.view.BaseViewModel
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
    }

    fun search(keyword: String) {
        setData { copy(keyword = keyword) }
    }
}