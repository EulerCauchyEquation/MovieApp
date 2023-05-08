package com.hwonchul.movie.domain.usecase.details;

import com.hwonchul.movie.domain.model.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 영화 예고편 가져오기 UseCase
 */
public interface GetVideoListUseCase {

    Flowable<List<Video>> invoke(int movieId);
}
