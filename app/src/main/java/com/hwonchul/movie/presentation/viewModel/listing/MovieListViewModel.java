package com.hwonchul.movie.presentation.viewModel.listing;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.hwonchul.movie.R;
import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieListType;
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase;
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase;
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
public class MovieListViewModel extends ViewModel {
    private static final String KEY_LIST_TYPE = "MovieListType";

    private final SavedStateHandle savedStateHandle;

    // 영화 목록 결과 observer
    private final MutableLiveData<Result<List<Movie>>> loadMovieListResult = new SingleLiveEvent<>();
    private final MutableLiveData<List<Movie>> movieList = new SingleLiveEvent<>();

    private final GetMovieListUseCase getMovieListUseCase;
    private final RefreshMovieListUseCase refreshMovieListUseCase;

    private final CompositeDisposable compositeDisposable;

    @Inject
    public MovieListViewModel(@NonNull GetMovieListUseCase getMovieListUseCase,
                              @NonNull RefreshMovieListUseCase refreshMovieListUseCase,
                              @NonNull SavedStateHandle savedStateHandle) {
        this.getMovieListUseCase = getMovieListUseCase;
        this.refreshMovieListUseCase = refreshMovieListUseCase;
        this.savedStateHandle = savedStateHandle;
        this.compositeDisposable = new CompositeDisposable();

        loadData();
    }

    public LiveData<Result<List<Movie>>> getLoadMovieListResult() {
        return loadMovieListResult;
    }

    public LiveData<List<Movie>> getMovieList() {
        return movieList;
    }

    private void loadData() {
        final MovieListType listType = getListType();

        // init data
        loadMovieListByListType(listType);
        refreshMovieList(listType);
    }

    private MovieListType getListType() {
        MovieListType type = savedStateHandle.get(KEY_LIST_TYPE);
        Timber.d("listType : %s", type);
        return type == null ? MovieListType.NowPlaying : type;
    }

    @SuppressWarnings("unchecked")
    public void loadMovieListByListType(MovieListType listType) {
        loadMovieListResult.setValue(new Result.Loading());
        Disposable disposable = getMovieListUseCase.invoke(listType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        _movieList -> {
                            loadMovieListResult.setValue(new Result.Success<>(_movieList));
                            movieList.setValue(_movieList);
                        },
                        // 실패 시
                        throwable -> {
                            loadMovieListResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);
    }

    @SuppressWarnings("unchecked")
    private void refreshMovieList(MovieListType listType) {
        loadMovieListResult.setValue(new Result.Loading());
        Disposable disposable = refreshMovieListUseCase.invoke(listType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // 성공 시
                        () -> {
                        },
                        // 실패 시
                        throwable -> {
                            loadMovieListResult.setValue(new Result.Error(R.string.all_response_failed));
                            Timber.d(throwable);
                        });
        compositeDisposable.add(disposable);

        savedStateHandle.set(KEY_LIST_TYPE, listType);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
