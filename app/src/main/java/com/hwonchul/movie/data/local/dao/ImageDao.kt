package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hwonchul.movie.data.local.model.ImageEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ImageDao {

    @Query("SELECT * FROM " + ImageEntity.TABLE_NAME + " WHERE movie_id = :movieId")
    fun findImagesByMovieId(movieId: Int): Flowable<List<ImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<ImageEntity>): Completable
}