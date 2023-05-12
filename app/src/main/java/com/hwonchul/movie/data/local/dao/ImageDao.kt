package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.data.local.model.ImageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ImageDao {

    @Query("SELECT * FROM " + ImageEntity.TABLE_NAME + " WHERE movie_id = :movieId")
    fun findImagesByMovieId(movieId: Int): Flowable<List<ImageEntity>>

    @Upsert
    fun upsert(entities: List<ImageEntity>): Completable
}