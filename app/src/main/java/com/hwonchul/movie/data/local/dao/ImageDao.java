package com.hwonchul.movie.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hwonchul.movie.data.local.model.ImageEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface ImageDao {

    @Query("SELECT * FROM " + ImageEntity.TABLE_NAME + " WHERE movie_id = :movieId")
    Flowable<List<ImageEntity>> findImagesByMovieId(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ImageEntity> entities);
}
