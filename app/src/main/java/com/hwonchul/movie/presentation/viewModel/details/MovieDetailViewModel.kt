package com.hwonchul.movie.presentation.viewModel.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hwonchul.movie.R
import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieDetail
import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.usecase.details.GetMovieUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshImageListUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshMovieUseCase
import com.hwonchul.movie.domain.usecase.details.RefreshVideoListUseCase
import com.hwonchul.movie.util.Result
import com.hwonchul.movie.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMovieUseCase: GetMovieUseCase,
    private val refreshMovieUseCase: RefreshMovieUseCase,
    private val refreshImageListUseCase: RefreshImageListUseCase,
    private val refreshVideoListUseCase: RefreshVideoListUseCase
) : ViewModel() {

    private val _loadMovieResult: MutableLiveData<Result<MovieDetail>> = SingleLiveEvent()
    val loadMovieResult: MutableLiveData<Result<MovieDetail>> = _loadMovieResult

    private val _movie: MutableLiveData<MovieDetail> = SingleLiveEvent()
    val movie: MutableLiveData<MovieDetail> = _movie

    private val _videosResult: MutableLiveData<Result<List<Video>>> = SingleLiveEvent()
    val videosResult: MutableLiveData<Result<List<Video>>> = _videosResult

    private val _postersResult: MutableLiveData<Result<List<Video>>> = SingleLiveEvent()
    val postersResult: MutableLiveData<Result<List<Video>>> = _postersResult

    private val compositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    fun loadData() {
        val movie = savedStateHandle.get<Movie>(KEY_MOVIE)
        Timber.d("movie : %s", movie)

        // init data
        loadMovie(movie!!.id)
        refreshMovie(movie.id)
        refreshPhotos(movie.id)
        refreshVideos(movie.id)
    }

    private fun loadMovie(movieId: Int) {
        _loadMovieResult.value = Result.Loading()
        val disposable = getMovieUseCase(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {
                    _loadMovieResult.value = Result.Success(it)
                    _movie.setValue(it)
                },
                // 실패 시
                {
                    _loadMovieResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshMovie(movieId: Int) {
        val disposable = refreshMovieUseCase(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {},
                // 실패 시
                {
                    _loadMovieResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshPhotos(movieId: Int) {
        val disposable = refreshImageListUseCase(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {},
                // 실패 시
                {
                    _postersResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun refreshVideos(movieId: Int) {
        val disposable = refreshVideoListUseCase(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // 성공 시
                {},
                // 실패 시
                {
                    _videosResult.value = Result.Error(R.string.all_response_failed)
                    Timber.d(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val KEY_MOVIE = "movie"
    }
}