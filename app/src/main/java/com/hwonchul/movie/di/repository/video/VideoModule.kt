package com.hwonchul.movie.di.repository.video

import com.hwonchul.movie.data.mapper.VideoMapper
import com.hwonchul.movie.data.mapper.VideoMapperImpl
import com.hwonchul.movie.data.repository.VideoRepositoryImpl
import com.hwonchul.movie.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VideoModule {

    companion object {
        @Provides
        @Singleton
        fun provideVideoMapper(): VideoMapper = VideoMapperImpl()
    }

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        repository: VideoRepositoryImpl
    ): VideoRepository
}