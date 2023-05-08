package com.hwonchul.movie.domain.usecase.details;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.repository.MovieRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class RefreshMovieUseCaseImpl implements RefreshMovieUseCase {
   private final MovieRepository repository;

   @Inject
   public RefreshMovieUseCaseImpl(@NonNull MovieRepository repository) {
      this.repository = repository;
   }

   @Override
   public Completable invoke(int movieId) {
      return repository.refreshForMovie(movieId);
   }
}
