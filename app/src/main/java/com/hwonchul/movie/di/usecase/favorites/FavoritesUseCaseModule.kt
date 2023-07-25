package com.hwonchul.movie.di.usecase.favorites

import com.hwonchul.movie.domain.usecase.favorites.AddFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.AddFavoritesUseCaseImpl
import com.hwonchul.movie.domain.usecase.favorites.RefreshFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RefreshFavoritesUseCaseImpl
import com.hwonchul.movie.domain.usecase.favorites.RemoveFavoritesUseCase
import com.hwonchul.movie.domain.usecase.favorites.RemoveFavoritesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class FavoritesUseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindAddFavoritesUseCase(useCase: AddFavoritesUseCaseImpl): AddFavoritesUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindRefreshFavoritesUseCase(useCase: RefreshFavoritesUseCaseImpl): RefreshFavoritesUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindRemoveFavoritesUseCase(useCase: RemoveFavoritesUseCaseImpl): RemoveFavoritesUseCase
}