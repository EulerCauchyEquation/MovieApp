package com.hwonchul.movie.di.network

import com.hwonchul.movie.data.remote.api.tmdb.TMDBInterceptor
import com.hwonchul.movie.data.remote.api.tmdb.TMDBService
import com.hwonchul.movie.data.remote.api.firebase.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Named("TMDBUrl")
    @Provides
    fun provideTMDBBaseUrl() = TMDBService.URL

    @Singleton
    @Provides
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    @Named("TMDBClient")
    @Singleton
    @Provides
    fun provideTMDBOkHttpClient(logging: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(TMDBInterceptor())
            .addInterceptor(logging)
            .build()

    @Named("TMDBRetrofit")
    @Singleton
    @Provides
    fun provideTMDBRetrofit(
        @Named("TMDBClient") httpClient: OkHttpClient,
        @Named("TMDBUrl") baseUrl: String
    ): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideTMDBService(@Named("TMDBRetrofit") retrofit: Retrofit.Builder): TMDBService =
        retrofit.build().create(TMDBService::class.java)

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth()
}