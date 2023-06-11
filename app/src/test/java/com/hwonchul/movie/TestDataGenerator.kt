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
                releaseDate = LocalDate.now(),
                voteAverage = 0.0,
                popularity = Random.nextDouble(1000.0, 9999.0),
                mainPosterPath = "",
                mainBackdropPath = "",
                listType = listType,
            )
        }
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