package com.hwonchul.movie.domain.usecase.listing;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieListType;
import com.hwonchul.movie.domain.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetMovieListUseCaseImpl implements GetMovieListUseCase {
    private final MovieRepository repository;

    @Inject
    public GetMovieListUseCaseImpl(@NonNull MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<Movie>> invoke(MovieListType listType) {
        return repository.getAllMoviesByListType(listType);
    }
}
