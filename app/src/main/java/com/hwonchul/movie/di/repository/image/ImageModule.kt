package com.hwonchul.movie.di.repository.image

import com.hwonchul.movie.data.repository.ImageRepositoryImpl
import com.hwonchul.movie.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        repository: ImageRepositoryImpl
    ): ImageRepository
}