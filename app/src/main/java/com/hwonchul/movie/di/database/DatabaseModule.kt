package com.hwonchul.movie.di.database

import android.content.Context
import androidx.room.Room
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import com.hwonchul.movie.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMovieDataBase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            MovieDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration()
            .addTypeConverter(LocalDateConverter())
            .build()

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao = movieDatabase.movieDao()

    @Singleton
    @Provides
    fun provideMovieDetailDao(movieDatabase: MovieDatabase): MovieDetailDao = movieDatabase.movieDetailDao()

    @Singleton
    @Provides
    fun provideVideoDao(movieDatabase: MovieDatabase): VideoDao =
        movieDatabase.videoDao()

    @Singleton
    @Provides
    fun providePosterDao(movieDatabase: MovieDatabase): ImageDao =
        movieDatabase.posterDao()
}