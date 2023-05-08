package com.hwonchul.movie.domain.usecase.listing;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.model.MovieListType;
import com.hwonchul.movie.domain.repository.MovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class RefreshMovieListUseCaseImpl implements RefreshMovieListUseCase {
    private final MovieRepository repository;

    @Inject
    public RefreshMovieListUseCaseImpl(@NonNull MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable invoke(MovieListType listType) {
        return repository.refreshForMovieList(listType);
    }
}
