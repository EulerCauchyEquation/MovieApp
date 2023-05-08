package com.hwonchul.movie.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hwonchul.movie.data.local.converter.LocalDateConverter;
import com.hwonchul.movie.data.local.dao.MovieDao;
import com.hwonchul.movie.data.local.dao.ImageDao;
import com.hwonchul.movie.data.local.dao.VideoDao;
import com.hwonchul.movie.data.local.model.MovieDetailEntity;
import com.hwonchul.movie.data.local.model.MovieEntity;
import com.hwonchul.movie.data.local.model.ImageEntity;
import com.hwonchul.movie.data.local.model.VideoEntity;

@TypeConverters({LocalDateConverter.class})
@Database(entities = {
        MovieEntity.class,
        VideoEntity.class,
        ImageEntity.class},
        version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    public static final String DB_NAME = "local.db";

    public abstract MovieDao movieDao();

    public abstract VideoDao videoDao();

    public abstract ImageDao posterDao();
}