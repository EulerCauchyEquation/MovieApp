package com.hwonchul.movie.data.local.dao;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Upsert;

import com.hwonchul.movie.data.local.model.MovieEntity;
import com.hwonchul.movie.data.local.model.MovieProjection;
import com.hwonchul.movie.data.local.model.MovieWithMedia;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " ORDER BY popularity DESC")
     // 인기순으로 영화 목록 조회
    Flowable<List<MovieProjection>> findAllProjectionOrderByPopularity();


    @Transaction
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " WHERE id = :movieId")
     // 영화 상세 정보 조회
    Flowable<MovieWithMedia> findMovieDetailById(int movieId);

    @Upsert
    Completable upsertMovie(MovieEntity entity);

    @Upsert(entity = MovieEntity.class)
    Completable upsertMovieProjections(List<MovieProjection> projections);
}
