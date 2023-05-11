package com.hwonchul.movie.presentation.viewModel.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase
import com.hwonchul.movie.util.Result
import com.hwonchul.movie.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val refreshMovieListUseCase: RefreshMovieListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loadMovieListResult = SingleLiveEvent<Result<List<Movie>>>()
    val loadMovieListResult: LiveData<Result<List<Movie>>> = _loadMovieListResult

    private val _movieList = SingleLiveEvent<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    private fun loadData() {
        val listType = listType

        // init data
        loadMovieListByListType(listType)
        refreshMovieList(listType)
    }

    private val listType: MovieListType
        get() {
            val type = savedStateHandle.get<MovieListType>(KEY_LIST_TYPE)
            Timber.d("listType : %s", type)
            return type ?: MovieListType.NowPlaying
        }

    fun loadMovieListByListType(listType: MovieListType) {
        _loadMovieListResult.value = Result.Loading()
        val disposable = getMovieListUseCase(listType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {
                    _loadMovieListResult.value = Result.Success(it)
                    _movieList.value = it
                },
                // 실패 시
                { throwable: Throwable ->
                    _loadMovieListResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(throwable)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshMovieList(listType: MovieListType) {
        val disposable = refreshMovieListUseCase(listType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {},
                // 실패 시
                { throwable: Throwable ->
                    _loadMovieListResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(throwable)
                }
            )
        compositeDisposable.add(disposable)
        savedStateHandle[KEY_LIST_TYPE] = listType
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val KEY_LIST_TYPE = "MovieListType"
    }
}