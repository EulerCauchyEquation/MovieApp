package com.hwonchul.movie.data.remote.api.tmdb

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hwonchul.movie.FileReader
import com.hwonchul.movie.data.remote.model.ImageResponse
import com.hwonchul.movie.data.remote.model.MovieDto
import com.hwonchul.movie.data.remote.model.MovieProjectionDto
import com.hwonchul.movie.data.remote.model.VideoDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TmdbApiTest {
    private lateinit var service: TMDBService
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logging)
            .addInterceptor(TMDBInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/").toString())
            .client(httpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(TMDBService::class.java)

        println("server 초기화 완료")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getNowPlayingList 테스트`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(successJsonStringWithPopularPath)

        server.enqueue(response)

        val result = service.getNowPlayingList()
        val expected: MutableList<MovieProjectionDto> = Gson().fromJson(
            expectedJsonStringWithPopularPath,
            object : TypeToken<MutableList<MovieProjectionDto>>() {}.type
        )
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getMovie 테스트`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(successJsonStringWithMovieDetailPath)

        server.enqueue(response)

        val result = service.getMovie(testMovieId)
        val expected = Gson().fromJson(expectedJsonStringWithMovieDetailPath, MovieDto::class.java)
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getVideoList 테스트`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(successJsonStringWithVideosPath)

        server.enqueue(response)

        val result = service.getVideoList(testMovieId)
        val expected: MutableList<VideoDto> = Gson().fromJson(
            expectedJsonStringWithVideosPath,
            object : TypeToken<MutableList<VideoDto>>() {}.type
        )
        assertEquals(expected, result)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getImageList 테스트`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .setBody(successJsonStringWithImagesPath)

        server.enqueue(response)

        val result = service.getImageList(testMovieId)
        val expected = Gson().fromJson(successJsonStringWithImagesPath, ImageResponse::class.java)
        assertEquals(expected, result)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    companion object {
        const val testMovieId = 1

        private val successJsonStringWithPopularPath =
            FileReader.readStringFromFile("text_data/tmdb/movieList/success.json").trimIndent()

        private val successJsonStringWithMovieDetailPath =
            FileReader.readStringFromFile("text_data/tmdb/movie/success.json").trimIndent()

        private val successJsonStringWithVideosPath =
            FileReader.readStringFromFile("text_data/tmdb/videos/success.json").trimIndent()

        private val successJsonStringWithImagesPath =
            FileReader.readStringFromFile("text_data/tmdb/image/success.json").trimIndent()

        private val expectedJsonStringWithPopularPath =
            FileReader.readStringFromFile("text_data/tmdb/movieList/success_filtered.json")
                .trimIndent()

        private val expectedJsonStringWithMovieDetailPath =
            FileReader.readStringFromFile("text_data/tmdb/movie/success_filtered.json").trimIndent()

        private val expectedJsonStringWithVideosPath =
            FileReader.readStringFromFile("text_data/tmdb/videos/success_filtered.json")
                .trimIndent()
    }
}