package com.hwonchul.movie.domain.usecase.details;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.model.MovieDetail;
import com.hwonchul.movie.domain.repository.MovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetMovieUseCaseImpl implements GetMovieUseCase {
    private final MovieRepository repository;

    @Inject
    public GetMovieUseCaseImpl(@NonNull MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<MovieDetail> invoke(int movieId) {
        return repository.getMovieDetailById(movieId);
    }
}
