package com.hwonchul.movie.domain.repository;

import com.hwonchul.movie.domain.model.Video;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface VideoRepository {

    /**
     * 영화클립 가져오기
     */
    Flowable<List<Video>> getAllVideosByMovieId(int movieId);

    /**
     * API 에 최신 데이터를 요청
     */
    Completable refreshFromRemote(int movieId);
}
