package com.hwonchul.movie.di.usecase.auth

import com.hwonchul.movie.domain.usecase.auth.*
import com.hwonchul.movie.domain.usecase.login.SignInWithPhoneAuthUseCase
import com.hwonchul.movie.domain.usecase.login.SignInWithPhoneAuthUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhoneAuthUseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindSaveCommentUseCase(useCase: VerifyPhoneNumberUseCaseImpl): VerifyPhoneNumberUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindValidateSmsCodeUseCase(useCase: ValidateSmsCodeUseCaseImpl): ValidateSmsCodeUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindValidatePhoneNumberUseCase(useCase: ValidatePhoneNumberUseCaseImpl): ValidatePhoneNumberUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindTimerUseCase(useCase: TimerUseCaseImpl): TimerUseCase
}