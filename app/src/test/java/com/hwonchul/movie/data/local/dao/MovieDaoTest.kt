package com.hwonchul.movie.data.local.dao

import com.hwonchul.movie.TestDataGenerator
import com.hwonchul.movie.data.local.MovieDatabase
import com.hwonchul.movie.di.database.DatabaseModule
import com.hwonchul.movie.domain.model.MovieListType
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
    private lateinit var movieDetailDao: MovieDetailDao

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this, relaxed = true)

        movieDao = db.movieDao()
        movieDetailDao = db.movieDetailDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `findAllMovieOrderByPopularity 테스트`() = runTest {
        // given : movies 삽입
        movieDao.upsert(INSERTED_MOVIES)

        // when : NowPlaying 타입인 findAllMovieOrderByPopularity
        val targetListType = MovieListType.NowPlaying
        val movies = movieDao.findAllMovieOrderByPopularity(targetListType).first()

        // then : NowPlaying 타입인 영화 리스트를 반환
        val expected = INSERTED_MOVIES.filter { it.listType == targetListType }
            .sortedByDescending { it.popularity }
        assertEquals(expected, movies)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `기존과 새로운 MovieEntity 리스트로 upsert 테스트`() = runTest {
        // given : movies 삽입
        movieDao.upsert(INSERTED_MOVIES)

        // when : 추가 삽입
        movieDao.upsert(FETCHED_MOVIES)

        // then : 해당 타입인 리스트를 모두 가져오기
        val nowPlayingTypeActual =
            movieDao.findAllMovieOrderByPopularity(MovieListType.NowPlaying).first()

        val expected = FETCHED_MOVIES
            .filter { it.listType == MovieListType.NowPlaying }
            .sortedByDescending { it.popularity }
        assertEquals(expected, nowPlayingTypeActual)
    }

    @After
    fun tearDown() {
        db.close()
    }

    companion object {

        private const val MOVIE_ID = 1000
        private val INSERTED_MOVIES = TestDataGenerator.createMovieEntities(10)
        private val FETCHED_MOVIES = INSERTED_MOVIES +
                TestDataGenerator.createMovieEntities(10)
    }
}
