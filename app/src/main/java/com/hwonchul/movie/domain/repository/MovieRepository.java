package com.hwonchul.movie.domain.repository;

import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieDetail;
import com.hwonchul.movie.domain.model.MovieListType;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface MovieRepository {

    // 영화 상세 가져오기
    Flowable<MovieDetail> getMovieDetailById(int movieId);

    // 영화 리스트 가져오기
    Flowable<List<Movie>> getAllMoviesByListType(MovieListType listType);

    // 영화 리스트를 최신 데이터로
    Completable refreshForMovieList(MovieListType listType);

    // 영화 정보를 최신 데이터로
    Completable refreshForMovie(int movieId);
}
