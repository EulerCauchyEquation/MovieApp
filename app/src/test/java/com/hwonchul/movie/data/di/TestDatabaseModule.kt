package com.hwonchul.movie.data.di

import android.content.Context
import androidx.room.Room
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import com.hwonchul.movie.data.local.dao.ImageDao
import com.hwonchul.movie.data.local.dao.MovieDao
import com.hwonchul.movie.data.local.dao.MovieDetailDao
import com.hwonchul.movie.data.local.dao.VideoDao
import com.hwonchul.movie.di.database.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Singleton
    @Provides
    fun provideTestDataBase(@ApplicationContext context: Context): MovieDatabase =
        Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
            .addTypeConverter(LocalDateConverter())
            // 메인 스레드에서 access 가능
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao = movieDatabase.movieDao()

    @Singleton
    @Provides
    fun provideMovieDetailDao(movieDatabase: MovieDatabase): MovieDetailDao =
        movieDatabase.movieDetailDao()

    @Singleton
    @Provides
    fun provideVideoDao(movieDatabase: MovieDatabase): VideoDao =
        movieDatabase.videoDao()

    @Singleton
    @Provides
    fun providePosterDao(movieDatabase: MovieDatabase): ImageDao =
        movieDatabase.posterDao()
}
