package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.data.local.model.VideoEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface VideoDao {
    @Query(
        "SELECT * FROM " + VideoEntity.TABLE_NAME + " WHERE movie_id = :movieId " +
                "ORDER BY published_at DESC"
    )
    fun findVideosByMovieId(movieId: Int): Flowable<List<VideoEntity>>

    @Upsert
    fun upsert(entities: List<VideoEntity>): Completable
}