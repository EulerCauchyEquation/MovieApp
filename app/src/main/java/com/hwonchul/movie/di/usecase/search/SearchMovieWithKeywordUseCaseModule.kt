package com.hwonchul.movie.di.usecase.search

import com.hwonchul.movie.domain.usecase.search.SearchMovieWithKeywordUseCase
import com.hwonchul.movie.domain.usecase.search.SearchMovieWithKeywordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchMovieWithKeywordUseCaseModule {

    @Singleton
    @Binds
    abstract fun bindSearchMovieWithKeywordUseCase(useCase: SearchMovieWithKeywordUseCaseImpl): SearchMovieWithKeywordUseCase
}