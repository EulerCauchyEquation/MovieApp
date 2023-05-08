package com.hwonchul.movie.di.usecase.account

import com.hwonchul.movie.domain.usecase.account.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AccountUseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindGetUserInfoUseCase(useCase: GetCurrentUserInfoUseCaseImpl): GetCurrentUserInfoUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindLogOutUseCase(useCase: LogOutUseCaseImpl): LogOutUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindWithdrawalUseCase(useCase: WithdrawalUseCaseImpl): WithdrawalUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindRefreshUserInfoUseCase(useCase: RefreshUserInfoUseCaseImpl): RefreshUserInfoUseCase
}