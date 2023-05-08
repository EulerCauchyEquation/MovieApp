package com.hwonchul.movie.di.repository.user

import com.hwonchul.movie.data.local.source.UserLocalDataSource
import com.hwonchul.movie.data.local.source.UserLocalDataSourceImpl
import com.hwonchul.movie.data.remote.source.UserRemoteDataSource
import com.hwonchul.movie.data.remote.source.UserRemoteDataSourceImpl
import com.hwonchul.movie.data.repository.UserRepositoryImpl
import com.hwonchul.movie.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserRemoteSource(
        remoteDataSource: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserLocalSource(
        localDataSource: UserLocalDataSourceImpl
    ): UserLocalDataSource

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        repository: UserRepositoryImpl
    ): UserRepository
}