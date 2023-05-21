package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.data.local.model.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query(
        "SELECT * FROM " + VideoEntity.TABLE_NAME + " WHERE movie_id = :movieId " +
                "ORDER BY published_at DESC"
    )
    fun findVideosByMovieId(movieId: Int): Flow<List<VideoEntity>>

    @Upsert
    suspend fun upsert(entities: List<VideoEntity>)
}