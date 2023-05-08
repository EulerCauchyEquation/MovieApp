package com.hwonchul.movie.di.network

import android.content.Context
import com.hwonchul.movie.util.NetworkStatusHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkStatusHelperModule {

    @Singleton
    @Provides
    fun provideNetworkStatusHelper(@ApplicationContext context: Context): NetworkStatusHelper =
        NetworkStatusHelper(context.applicationContext)
}