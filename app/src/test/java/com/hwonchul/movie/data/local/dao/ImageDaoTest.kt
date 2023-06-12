package com.hwonchul.movie.data.local.dao

import com.hwonchul.movie.TestDataGenerator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.data.local.model.ImageEntity.Type
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
import org.junit.Assert.*
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
class ImageDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MovieDatabase
    private lateinit var imageDao: ImageDao
    private lateinit var movieDetailDao: MovieDetailDao

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this, relaxed = true)

        movieDetailDao = db.movieDetailDao()
        imageDao = db.posterDao()

        // 테스트 스레드를 블로킹하고 완료될 때까지 대기
        runBlocking {
            movieDetailDao.upsert(MOVIE)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findImagesByMovieId 테스트`() = runTest {
        // given : images 삽입
        imageDao.upsert(INSERTED_IMAGES)

        // when : findImagesByMovieId
        val actual = imageDao.findImagesByMovieId(MOVIE_ID).first()

        // then : MOVIE_ID 인 image들을 반환
        assertEquals(INSERTED_IMAGES, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `존재하지 않는 movieId로 findImagesByMovieId 테스트`() = runTest {
        // given : images 삽입
        imageDao.upsert(INSERTED_IMAGES)

        // when : findImagesByMovieId
        val images = imageDao.findImagesByMovieId(9999).first()

        // then : 빈 images를 반환
        assertTrue(images.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `기존과 새로운 이미지들로 upsert 테스트`() = runTest {
        // given : images 삽입
        imageDao.upsert(INSERTED_IMAGES)

        // when : 추가 삽입
        imageDao.upsert(FETCHED_IMAGES)

        // then : 추가 삽입까지 반영된 images를 반환
        val images = imageDao.findImagesByMovieId(MOVIE_ID).first()
        assertEquals(FETCHED_IMAGES, images)
    }

    @After
    fun tearDown() {
        db.close()
    }

    companion object {

        private const val MOVIE_ID = 1000
        private val INSERTED_IMAGES = TestDataGenerator.createImageEntities(3, MOVIE_ID)
        private val FETCHED_IMAGES = INSERTED_IMAGES.map { it.copy(imageType = Type.Poster) } +
                TestDataGenerator.createImageEntities(1, MOVIE_ID)

        val MOVIE = mockk<MovieDetailEntity>(relaxed = true) {
            every { id } returns MOVIE_ID
        }
    }
}
