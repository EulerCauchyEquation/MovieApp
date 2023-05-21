package com.hwonchul.movie.di.usecase.login

import com.hwonchul.movie.domain.usecase.login.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class LoginUseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun bindSignInWithPhoneAuthUseCase(useCase: SignInWithPhoneAuthUseCaseImpl): SignInWithPhoneAuthUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindRegisterUserUseCase(useCase: RegisterUserUseCaseImpl): RegisterUserUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindCheckUserRegistrationUseCase(useCase: CheckUserRegistrationUseCaseImpl): CheckUserRegistrationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindValidateNickNameUseCase(useCase: ValidateNickNameUseCaseImpl): ValidateNickNameUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindValidateUniqueNickNameUseCase(useCase: CheckDuplicateNickNameUseCaseImpl): CheckDuplicateNickNameUseCase
}