package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao : BaseDao<VideoEntity> {
    @Query(
        "SELECT * FROM " + VideoEntity.TABLE_NAME + " WHERE movie_id = :movieId " +
                "ORDER BY published_at DESC"
    )
    fun findVideosByMovieId(movieId: Int): Flow<List<VideoEntity>>

    suspend fun upsert(entity: VideoEntity) {
        try {
            insert(entity)
        } catch (e: SQLiteConstraintException) {
            update(entity)
        }
    }

    suspend fun upsert(entities: List<VideoEntity>) {
        entities.forEach { entity ->
            try {
                insert(entity)
            } catch (e: SQLiteConstraintException) {
                update(entity)
            }
        }
    }
}