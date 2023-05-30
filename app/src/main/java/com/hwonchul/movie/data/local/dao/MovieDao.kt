package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieProjection
import com.hwonchul.movie.data.local.model.MovieWithMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : BaseDao<MovieEntity> {
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " ORDER BY popularity DESC")
    // 인기순으로 영화 목록 조회
    fun findAllProjectionOrderByPopularity(): Flow<List<MovieProjection>>

    @Transaction
    @Query("SELECT * FROM " + MovieEntity.TABLE_NAME + " WHERE id = :movieId")
    // 영화 상세 정보 조회
    fun findMovieDetailById(movieId: Int): Flow<MovieWithMedia>

    @Insert(entity = MovieEntity::class)
    suspend fun insertMovieProjection(entity: MovieProjection)

    @Update(entity = MovieEntity::class)
    suspend fun updateMovieProjection(entity: MovieProjection)

    suspend fun upsertMovieProjection(entity: MovieProjection) {
        try {
            insertMovieProjection(entity)
        } catch (e: SQLiteConstraintException) {
            updateMovieProjection(entity)
        }
    }

    suspend fun upsertMovieProjection(entities: List<MovieProjection>) {
        entities.forEach { entity ->
            try {
                insertMovieProjection(entity)
            } catch (e: SQLiteConstraintException) {
                updateMovieProjection(entity)
            }
        }
    }

    suspend fun upsertMovie(entity: MovieEntity) {
        try {
            insert(entity)
        } catch (e: SQLiteConstraintException) {
            update(entity)
        }
    }
}