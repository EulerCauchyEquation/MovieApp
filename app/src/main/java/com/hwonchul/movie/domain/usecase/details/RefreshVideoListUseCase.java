package com.hwonchul.movie.domain.usecase.details;

import io.reactivex.rxjava3.core.Completable;

/**
 * 영화 동영상(예고편) 새로고침 usecase
 */
public interface RefreshVideoListUseCase {

    Completable invoke(int movieId);
}
