package com.hwonchul.movie

import com.hwonchul.movie.data.local.model.ImageEntity
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieProjection
import com.hwonchul.movie.data.local.model.VideoEntity
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

object TestDataGenerator {

    fun createMovieEntity(movieId: Int): MovieEntity {
        return MovieEntity(
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

    fun createMovieProjections(size: Int): List<MovieProjection> {
        return List(size) { i ->
            MovieProjection(
                id = UUID.randomUUID().hashCode(),
                title = "Title",
                originalTitle = "Title",
                releaseDate = LocalDate.now(),
                voteAverage = 0.0,
                popularity = Random.nextDouble(1000.0, 9999.0),
                mainPosterPath = "",
                mainBackdropPath = "",
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