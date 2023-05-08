package com.hwonchul.movie.domain.usecase.details;

import io.reactivex.rxjava3.core.Completable;

/**
 * 영화 사진들 새로고침 usecase
 */
public interface RefreshImageListUseCase {

    Completable invoke(int movieId);
}
