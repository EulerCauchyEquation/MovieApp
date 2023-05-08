package com.hwonchul.movie.data.repository;

import androidx.annotation.NonNull;

import com.hwonchul.movie.data.local.dao.MovieDao;
import com.hwonchul.movie.data.mapper.MovieMapper;
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi;
import com.hwonchul.movie.data.remote.model.MovieDto;
import com.hwonchul.movie.data.remote.model.MovieProjectionDto;
import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieDetail;
import com.hwonchul.movie.domain.model.MovieListType;
import com.hwonchul.movie.domain.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class MovieRepositoryImpl implements MovieRepository {
    private final TMDBApi api;
    private final MovieDao movieDao;
    private final MovieMapper mapper;

    @Inject
    public MovieRepositoryImpl(@NonNull TMDBApi api,
                               @NonNull MovieDao movieDao,
                               @NonNull MovieMapper mapper) {
        this.api = api;
        this.movieDao = movieDao;
        this.mapper = mapper;
    }

    @Override
    public Flowable<MovieDetail> getMovieDetailById(int movieId) {
        return movieDao.findMovieDetailById(movieId).map(mapper::toMovieDetail);
    }

    @Override
    public Flowable<List<Movie>> getAllMoviesByListType(MovieListType listType) {
        return movieDao.findAllProjectionOrderByPopularity().map(mapper::toMovies);
    }

    @Override
    public Completable refreshForMovieList(MovieListType listType) {
        return api.getNowPlayingMovieList().flatMapCompletable(this::saveToLocal);
    }

    @Override
    public Completable refreshForMovie(int movieId) {
        return api.getMovie(movieId).flatMapCompletable(this::saveToLocal);
    }

    private Completable saveToLocal(List<MovieProjectionDto> dtos) {
        return Single.just(dtos)
                .map(mapper::toMovieProjections)
                .flatMapCompletable(movieDao::upsertMovieProjections);
    }

    private Completable saveToLocal(MovieDto dto) {
        return Single.just(dto)
                .map(mapper::toEntity)
                .flatMapCompletable(movieDao::upsertMovie);
    }
}
