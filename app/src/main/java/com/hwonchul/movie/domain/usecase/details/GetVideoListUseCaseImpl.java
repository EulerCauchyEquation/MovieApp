package com.hwonchul.movie.domain.usecase.details;

import androidx.annotation.NonNull;

import com.hwonchul.movie.domain.model.Video;
import com.hwonchul.movie.domain.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class GetVideoListUseCaseImpl implements GetVideoListUseCase {
    private final VideoRepository repository;

    @Inject
    public GetVideoListUseCaseImpl(@NonNull VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<Video>> invoke(int movieId) {
        return repository.getAllVideosByMovieId(movieId);
    }
}
