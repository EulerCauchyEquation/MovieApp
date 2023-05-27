package com.hwonchul.movie.data.local.dao

import com.hwonchul.movie.TestDataGenerator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.di.database.DatabaseModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
class MovieDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: MovieDatabase
    private lateinit var movieDao: MovieDao
    private lateinit var videoDao: VideoDao
    private lateinit var imageDao: ImageDao

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this, relaxed = true)

        movieDao = db.movieDao()
        videoDao = db.videoDao()
        imageDao = db.posterDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findAllProjectionOrderByPopularity 테스트`() = runTest {
        // given : movie 삽입
        movieDao.upsertMovieProjection(INSERTED_MOVIE_PROJECTION)

        // when : findAllProjectionOrderByPopularity
        val movie = movieDao.findAllProjectionOrderByPopularity().first()

        // then : Popularity 를 내림차순으로 MovieProjection 타입인 아이템들을 반환
        val expected = INSERTED_MOVIE_PROJECTION.sortedByDescending { it.popularity }
        assertEquals(expected, movie)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findMovieDetailById 테스트`() = runTest {
        // given : movie, videos, images 삽입
        movieDao.upsertMovie(INSERTED_MOVIE)
        imageDao.upsert(INSERTED_IMAGES)
        videoDao.upsert(INSERTED_VIDEOS)

        // when : findMovieDetailById
        val movie = movieDao.findMovieDetailById(MOVIE_ID).first()

        // then : MOVIE_ID 의 MovieWithMedia 반환
        assertEquals(MOVIE_ID, movie.movie.id)
        assertEquals(INSERTED_IMAGES, movie.images)
        assertEquals(INSERTED_VIDEOS, movie.videos)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `존재하지 않는 movieId로 findMovieDetailById 테스트`() = runTest {
        // given : movie, videos, images 삽입
        movieDao.upsertMovie(INSERTED_MOVIE)
        imageDao.upsert(INSERTED_IMAGES)
        videoDao.upsert(INSERTED_VIDEOS)

        // when : findMovieDetailById
        val movie = movieDao.findMovieDetailById(9999).first()

        // then : 빈 MovieWithMedia 반환
        assertNull(movie)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `영화 삭제시 관련된 컨텐츠 모두 삭제 테스트 `() = runTest {
        // given : movie, videos, images 삽입
        movieDao.upsertMovie(INSERTED_MOVIE)
        imageDao.upsert(INSERTED_IMAGES)
        videoDao.upsert(INSERTED_VIDEOS)

        // when : delete
        movieDao.delete(INSERTED_MOVIE)

        // then : 해당 MOVIE 의 관련된 데이터까지 모두 삭제된다
        val movie = movieDao.findMovieDetailById(INSERTED_MOVIE.id).first()
        val videos = videoDao.findVideosByMovieId(INSERTED_MOVIE.id).first()
        val images = imageDao.findImagesByMovieId(INSERTED_MOVIE.id).first()
        assertNull(movie)
        assertTrue(videos.isEmpty())
        assertTrue(images.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `기존과 새로운 MovieProjection들로 upsert 테스트`() = runTest {
        // given : MovieProjection 삽입
        movieDao.upsertMovieProjection(INSERTED_MOVIE_PROJECTION)

        // when : 추가 삽입
        movieDao.upsertMovieProjection(FETCHED_MOVIE_PROJECTION)

        // then : 추가 삽입까지 반영된 영화 리스트를 반환
        val actual = movieDao.findAllProjectionOrderByPopularity().first()
        val expected = FETCHED_MOVIE_PROJECTION.sortedByDescending { it.popularity }
        assertEquals(expected, actual)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `기존과 새로운 MovieEntity로 upsert 테스트`() = runTest {
        // given : MovieEntity 삽입
        val movieId = INSERTED_MOVIE.id
        movieDao.upsertMovie(INSERTED_MOVIE)

        // when : 수정된 MovieEntity 삽입
        val updatedTitle = "updatedTitle"
        val updated = INSERTED_MOVIE.copy(title = updatedTitle)
        movieDao.upsertMovie(updated)

        // then : 수정된 영화를 반환
        val actual = movieDao.findMovieDetailById(movieId).first()
        assertEquals(movieId, actual.movie.id)
        assertEquals(updatedTitle, actual.movie.title)
    }

    @After
    fun tearDown() {
        db.close()
    }

    companion object {

        private const val MOVIE_ID = 1000
        private val INSERTED_IMAGES = TestDataGenerator.createImageEntities(3, MOVIE_ID)
        private val INSERTED_VIDEOS = TestDataGenerator.createVideoEntities(3, MOVIE_ID)
            .sortedByDescending { it.publishedAt }
        private val INSERTED_MOVIE = TestDataGenerator.createMovieEntity(MOVIE_ID)

        private val INSERTED_MOVIE_PROJECTION = TestDataGenerator.createMovieProjections(2)
        private val FETCHED_MOVIE_PROJECTION = INSERTED_MOVIE_PROJECTION +
                TestDataGenerator.createMovieProjections(2)
    }
}
