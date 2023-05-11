package com.hwonchul.movie.di.repository.image

import com.hwonchul.movie.data.mapper.ImageMapper
import com.hwonchul.movie.data.mapper.ImageMapperImpl
import com.hwonchul.movie.data.repository.ImageRepositoryImpl
import com.hwonchul.movie.domain.repository.ImageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageModule {

    companion object {
        @Provides
        @Singleton
        fun provideImageMapper(): ImageMapper =            ImageMapperImpl()
    }

    @Binds
    @Singleton
    abstract fun bindImageRepository(
        repository: ImageRepositoryImpl
    ): ImageRepository
}