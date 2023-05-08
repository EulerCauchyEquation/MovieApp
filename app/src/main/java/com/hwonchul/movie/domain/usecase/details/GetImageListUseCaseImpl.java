package com.hwonchul.movie.domain.usecase.details;

import com.hwonchul.movie.domain.model.Image;
import com.hwonchul.movie.domain.repository.ImageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;

public class GetImageListUseCaseImpl implements GetImageListUseCase {
    private final ImageRepository repository;

    @Inject
    public GetImageListUseCaseImpl(@NonNull ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<Image>> invoke(int movieId) {
        return repository.getAllPhotosByMovieId(movieId);
    }
}
