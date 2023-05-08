package com.hwonchul.movie.domain.usecase.details;

import io.reactivex.rxjava3.core.Completable;

/**
 * 영화상세 새로고침 usecase
 */
public interface RefreshMovieUseCase {

    Completable invoke(int movieId);
}
