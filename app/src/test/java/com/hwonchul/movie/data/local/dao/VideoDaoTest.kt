package com.hwonchul.movie.data.local.dao

import com.hwonchul.movie.TestDataGenerator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.model.MovieDetailEntity
import com.hwonchul.movie.di.database.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@UninstallModules(DatabaseModule::class)
class VideoDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MovieDatabase
    private lateinit var videoDao: VideoDao
    private lateinit var movieDetailDao: MovieDetailDao

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this, relaxed = true)

        movieDetailDao = db.movieDetailDao()
        videoDao = db.videoDao()

        runBlocking {
            movieDetailDao.upsert(MOVIE)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findVideosByMovieId 테스트`() = runTest {
        // given : videos 삽입
        videoDao.upsert(INSERTED_VIDEOS)

        // when : findVideosByMovieId
        val videos = videoDao.findVideosByMovieId(MOVIE_ID).first()

        // then : MOVIE_ID 인 videos들을 반환
        Assert.assertEquals(INSERTED_VIDEOS, videos)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `존재하지 않는 movieId로 findVideosByMovieId 테스트`() = runTest {
        // given : videos 삽입
        videoDao.upsert(INSERTED_VIDEOS)

        // when : findVideosByMovieId
        val videos = videoDao.findVideosByMovieId(9999).first()

        // then : 빈 videos를 반환
        Assert.assertTrue(videos.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `기존과 새로운 영상들로 upsert 테스트`() = runTest {
        // given : videos 삽입
        videoDao.upsert(INSERTED_VIDEOS)

        // when : 추가 삽입
        videoDao.upsert(FETCHED_VIDEOS)

        // then : 추가 삽입까지 반영된 videos를 반환
        val videos = videoDao.findVideosByMovieId(MOVIE_ID).first()
        Assert.assertEquals(FETCHED_VIDEOS, videos)
    }

    @After
    fun tearDown() {
        db.close()
    }

    companion object {
        private const val MOVIE_ID = 5
        val MOVIE = mockk<MovieDetailEntity>(relaxed = true) {
            every { id } returns MOVIE_ID
        }

        private val INSERTED_VIDEOS = TestDataGenerator.createVideoEntities(3, MOVIE_ID)
            .sortedByDescending { it.publishedAt }
        private val FETCHED_VIDEOS =
            (INSERTED_VIDEOS + TestDataGenerator.createVideoEntities(2, MOVIE_ID))
                .sortedByDescending { it.publishedAt }
    }
}
