package com.hwonchul.movie.domain.repository;

import com.hwonchul.movie.domain.model.Image;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface ImageRepository {

    /**
     * 스틸컷 가져오기
     */
    Flowable<List<Image>> getAllPhotosByMovieId(int movieId);

    /**
     * API 에 최신 데이터를 요청
     */
    Completable refreshFromRemote(int movieId);
}
