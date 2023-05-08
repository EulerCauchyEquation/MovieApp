package com.hwonchul.movie.domain.usecase.details;

import com.hwonchul.movie.domain.model.Image;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 영화 스틸컷 가져오기 Usecase
 */
public interface GetImageListUseCase {

    Flowable<List<Image>> invoke(int movieId);
}
