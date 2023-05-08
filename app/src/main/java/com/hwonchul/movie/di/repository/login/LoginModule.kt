package com.hwonchul.movie.di.repository.login

import com.hwonchul.movie.data.repository.LoginRepositoryImpl
import com.hwonchul.movie.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        repository: LoginRepositoryImpl
    ): LoginRepository
}