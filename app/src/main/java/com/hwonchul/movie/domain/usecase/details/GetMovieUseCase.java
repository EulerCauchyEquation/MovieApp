package com.hwonchul.movie.domain.usecase.details;

import com.hwonchul.movie.domain.model.MovieDetail;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 영화상세 item 가져오기 Usecase
 */
public interface GetMovieUseCase {

    Flowable<MovieDetail> invoke(int movieId);
}
