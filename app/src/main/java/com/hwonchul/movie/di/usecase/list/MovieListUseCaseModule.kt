package com.hwonchul.movie.di.usecase.list

import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.GetMovieListUseCaseImpl
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCase
import com.hwonchul.movie.domain.usecase.listing.RefreshMovieListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieListUseCaseModule {

    @Singleton
    @Binds
    abstract fun bindGetMovieListUseCase(useCase: GetMovieListUseCaseImpl): GetMovieListUseCase

    @Singleton
    @Binds
    abstract fun bindRefreshMovieListUseCase(useCase: RefreshMovieListUseCaseImpl): RefreshMovieListUseCase
}