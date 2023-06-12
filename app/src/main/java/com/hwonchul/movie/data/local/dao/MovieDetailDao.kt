package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieDetailEntity
import com.hwonchul.movie.data.local.model.MovieDetailWithMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailDao : BaseDao<MovieDetailEntity> {

    @Transaction
    @Query("SELECT * FROM " + MovieDetailEntity.TABLE_NAME + " WHERE id = :movieId")
    fun findMovieDetailById(movieId: Int): Flow<MovieDetailWithMedia>

    suspend fun upsert(entity: MovieDetailEntity) {
        try {
            insert(entity)
        } catch (e: SQLiteConstraintException) {
            update(entity)
        }
    }
}