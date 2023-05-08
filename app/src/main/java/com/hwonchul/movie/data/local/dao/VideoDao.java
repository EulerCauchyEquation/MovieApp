package com.hwonchul.movie.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hwonchul.movie.data.local.model.VideoEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface VideoDao {

    @Query("SELECT * FROM " + VideoEntity.TABLE_NAME + " WHERE movie_id = :movieId " +
            "ORDER BY published_at DESC")
    Flowable<List<VideoEntity>> findVideosByMovieId(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<VideoEntity> entities);
}
