package com.hwonchul.movie.presentation.viewModel.details;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.hwonchul.movie.R;
import com.hwonchul.movie.domain.model.MovieDetail;
import com.hwonchul.movie.domain.model.Image;
import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.Video;
import com.hwonchul.movie.domain.usecase.details.GetMovieUseCase;
import com.hwonchul.movie.domain.usecase.details.RefreshMovieUseCase;
import com.hwonchul.movie.domain.usecase.details.RefreshImageListUseCase;
import com.hwonchul.movie.domain.usecase.details.RefreshVideoListUseCase;
import com.hwonchul.movie.util.Result;
import com.hwonchul.movie.util.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class MovieDetailViewModel extends ViewModel {
    private static final String KEY_MOVIE = "movie";

    private final SavedStateHandle savedStateHandle;

    private final MutableLiveData<Result<MovieDetail>> loadMovieResult = new SingleLiveEvent<>();
    private final MutableLiveData<MovieDetail> movie = new SingleLiveEvent<>();
    private final MutableLiveData<Result<List<Video>>> videosResult = new SingleLiveEvent<>();
    private final MutableLiveData<Result<List<Image>>> postersResult = new SingleLiveEvent<>();

    // UseCase
    private final GetMovieUseCase getMovieUseCase;
    private final RefreshMovieUseCase refreshMovieUseCase;
    private final RefreshImageListUseCase refreshImageListUseCase;
    private final RefreshVideoListUseCase refreshVideoListUseCase;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MovieDetailViewModel(@NonNull GetMovieUseCase getMovieUseCase,
                                @NonNull RefreshMovieUseCase refreshMovieUseCase,
                                @NonNull RefreshImageListUseCase refreshImageListUseCase,
                                @NonNull RefreshVideoListUseCase refreshVideoListUseCase,
                                @NonNull SavedStateHandle savedStateHandle) {
        this.getMovieUseCase = getMovieUseCase;
        this.refreshMovieUseCase = refreshMovieUseCase;
        this.refreshImageListUseCase = refreshImageListUseCase;
        this.refreshVideoListUseCase = refreshVideoListUseCase;
        this.savedStateHandle = savedStateHandle;

        loadData();
    }

    public LiveData<MovieDetail> getMovie() {
        return movie;
    }

    public LiveData<Result<MovieDetail>> getLoadMovieResult() {
        return loadMovieResult;
    }

    public void loadData() {
        Movie movie = savedStateHandle.get(KEY_MOVIE);
        Timber.d("movie : %s", movie);

        // init data
        loadMovie(movie.getId());
        refreshMovie(movie.getId());
        refreshPhotos(movie.getId());
        refreshVideos(movie.getId());
    }

    @SuppressWarnings("unchecked")
    private void loadMovie(int movieId) {
        loadMovieResult.setValue(new Result.Loading());
        Disposable disposable = getMovieUseCase.invoke(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        _movie -> {
                            loadMovieResult.setValue(new Result.Success<>(_movie));
                            movie.setValue(_movie);
                        },
                        // 실패 시
                        throwable -> {
                            loadMovieResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    private void refreshMovie(int movieId) {
        Disposable disposable = refreshMovieUseCase.invoke(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        () -> {
                        },
                        // 실패 시
                        throwable -> {
                            loadMovieResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    private void refreshPhotos(int movieId) {
        Disposable disposable = refreshImageListUseCase.invoke(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        () -> {
                        },
                        // 실패 시
                        throwable -> {
                            postersResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    private void refreshVideos(int movieId) {
        Disposable disposable = refreshVideoListUseCase.invoke(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        () -> {
                        },
                        // 실패 시
                        throwable -> {
                            videosResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}