package com.hwonchul.movie.domain.usecase.listing;

import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieListType;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

/**
 * 영화 목록 가져오기 Usecase
 */
public interface GetMovieListUseCase {

    Flowable<List<Movie>> invoke(MovieListType listType);
}
