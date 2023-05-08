package com.hwonchul.movie.di.usecase.account.profile

import com.hwonchul.movie.domain.usecase.account.UpdateUserUseCase
import com.hwonchul.movie.domain.usecase.account.UpdateUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ProfileUseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindUpdateUserUseCase(useCase: UpdateUserUseCaseImpl): UpdateUserUseCase
}