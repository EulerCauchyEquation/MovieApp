package com.hwonchul.movie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import com.hwonchul.movie.data.local.dao.ImageDao
import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.local.dao.MovieDetailDao
import com.hwonchul.movie.data.local.dao.VideoDao
import com.hwonchul.movie.data.local.model.ImageEntity
import com.hwonchul.movie.data.local.model.MovieDetailEntity
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.VideoEntity

@TypeConverters(LocalDateConverter::class)
@Database(
    entities = [MovieEntity::class, MovieDetailEntity::class, VideoEntity::class, ImageEntity::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieDetailDao(): MovieDetailDao
    abstract fun videoDao(): VideoDao
    abstract fun posterDao(): ImageDao

    companion object {
        const val DB_NAME = "local.db"
    }
}