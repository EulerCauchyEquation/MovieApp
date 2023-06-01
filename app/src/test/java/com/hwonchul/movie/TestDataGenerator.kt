package com.hwonchul.movie

import com.hwonchul.movie.data.local.model.ImageEntity
import com.hwonchul.movie.data.local.model.MovieDetailEntity
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.VideoEntity
import com.hwonchul.movie.domain.model.MovieListType
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

object TestDataGenerator {

    fun createMovieDetailEntity(movieId: Int): MovieDetailEntity {
        return MovieDetailEntity(
            id = movieId,
            title = "Title",
            originalTitle = "Title",
            releaseDate = LocalDate.now(),
            voteAverage = 0.0,
            popularity = Random.nextDouble(1000.0, 9999.0),
            mainPosterPath = "",
            mainBackdropPath = "",
            runtime = 0,
            overview = "",
            status = "",
            genres = "",
        )
    }

    fun createMovieEntities(
        size: Int,
        listType: MovieListType = MovieListType.NowPlaying
    ): List<MovieEntity> {
        return List(size) { i ->
            MovieEntity(
                id = UUID.randomUUID().hashCode(),
                title = "Title",
                originalTitle = "Title",
                releaseDate = createDate(),
                voteAverage = 0.0,
                popularity = Random.nextDouble(1000.0, 9999.0),
                mainPosterPath = "",
                mainBackdropPath = "",
                listType = listType,
            )
        }
    }

    private fun createDate(): LocalDate {
        val randomDays = Random.nextInt(21) - 10  // 현재 날짜에서 -10일부터 +10일 사이의 임의의 일수를 선택
        return LocalDate.now().plusDays(randomDays.toLong())
    }

    fun createImageEntities(size: Int, movieId: Int) =
        List(size) { i ->
            ImageEntity(
                path = "Path_${UUID.randomUUID()}_${i + 1}",
                movieId = movieId,
                imageType = if (i % 2 == 0) ImageEntity.Type.Backdrop else ImageEntity.Type.Poster,
            )
        }

    fun createVideoEntities(size: Int, movieId: Int): List<VideoEntity> {
        val date = LocalDate.now().plus(1, ChronoUnit.DAYS)

        return List(size) { i ->
            VideoEntity(
                id = "id_${UUID.randomUUID()}",
                title = "Title_${UUID.randomUUID()}_${i + 1}",
                movieId = movieId,
                publishedAt = date.plusDays(i.toLong()),
            )
        }
    }
}