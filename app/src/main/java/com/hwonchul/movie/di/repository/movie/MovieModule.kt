package com.hwonchul.movie.di.repository.movie

import com.hwonchul.movie.data.repository.MovieRepositoryImpl
import com.hwonchul.movie.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository
}