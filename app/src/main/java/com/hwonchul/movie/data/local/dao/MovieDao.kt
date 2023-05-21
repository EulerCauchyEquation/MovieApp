package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieProjection
import com.hwonchul.movie.data.local.model.MovieWithMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " ORDER BY popularity DESC")
    // 인기순으로 영화 목록 조회
    fun findAllProjectionOrderByPopularity(): Flow<List<MovieProjection>>

    @Transaction
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " WHERE id = :movieId")
    // 영화 상세 정보 조회
    fun findMovieDetailById(movieId: Int): Flow<MovieWithMedia>

    @Upsert
    suspend fun upsertMovie(entity: MovieEntity)

    @Upsert(entity = MovieEntity::class)
    suspend fun upsertMovieProjections(projections: List<MovieProjection>)
}