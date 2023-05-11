package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieProjection
import com.hwonchul.movie.data.local.model.MovieWithMedia
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MovieDao {
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " ORDER BY popularity DESC")
    // 인기순으로 영화 목록 조회
    fun findAllProjectionOrderByPopularity(): Flowable<List<MovieProjection>>

    @Transaction
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " WHERE id = :movieId")
    // 영화 상세 정보 조회
    fun findMovieDetailById(movieId: Int): Flowable<MovieWithMedia>

    @Upsert
    fun upsertMovie(entity: MovieEntity): Completable

    @Upsert(entity = MovieEntity::class)
    fun upsertMovieProjections(projections: List<MovieProjection>): Completable
}