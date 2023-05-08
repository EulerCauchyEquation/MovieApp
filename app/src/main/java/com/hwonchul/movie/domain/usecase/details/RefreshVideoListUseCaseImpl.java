package com.hwonchul.movie.domain.usecase.details;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.repository.VideoRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class RefreshVideoListUseCaseImpl implements RefreshVideoListUseCase {
    private final VideoRepository repository;

    @Inject
    public RefreshVideoListUseCaseImpl(@NonNull VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable invoke(int movieId) {
        return repository.refreshFromRemote(movieId);
    }
}
