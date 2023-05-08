package com.hwonchul.movie.domain.usecase.details;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.repository.ImageRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class RefreshImageListUseCaseImpl implements RefreshImageListUseCase {
    private final ImageRepository repository;

    @Inject
    public RefreshImageListUseCaseImpl(@NonNull ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Completable invoke(int movieId) {
        return repository.refreshFromRemote(movieId);
    }
}
