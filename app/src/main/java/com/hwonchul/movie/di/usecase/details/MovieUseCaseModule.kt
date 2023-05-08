package com.hwonchul.movie.di.usecase.details

import com.hwonchul.movie.domain.usecase.details.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieUseCaseModule {

    @Singleton
    @Binds
    abstract fun bindGetMovieUseCase(useCase: GetMovieUseCaseImpl): GetMovieUseCase

    @Singleton
    @Binds
    abstract fun bindGetMovieVideoListUseCase(
        useCase: GetVideoListUseCaseImpl
    ): GetVideoListUseCase

    @Singleton
    @Binds
    abstract fun bindGetMoviePhotoListUseCase(
        useCase: GetImageListUseCaseImpl
    ): GetImageListUseCase

    @Singleton
    @Binds
    abstract fun bindRefreshMovieUseCase(useCase: RefreshMovieUseCaseImpl): RefreshMovieUseCase

    @Singleton
    @Binds
    abstract fun bindRefreshPhotoListUseCase(
        useCase: RefreshImageListUseCaseImpl
    ): RefreshImageListUseCase


    @Singleton
    @Binds
    abstract fun bindRefreshVideoListUseCase(
        useCase: RefreshVideoListUseCaseImpl
    ): RefreshVideoListUseCase
}