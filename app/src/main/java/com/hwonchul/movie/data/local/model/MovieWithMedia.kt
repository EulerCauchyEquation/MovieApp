package com.hwonchul.movie.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.model.MovieDetail

// 무비클립, 이미지 리스트까지 모두 가져온 영화 정보
data class MovieWithMedia(
    @Embedded
    val movie: MovieEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id",
    )
    val images: List<ImageEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id",
    )
    val videos: List<VideoEntity>?,
)

fun MovieWithMedia.toDomain(): MovieDetail {
    return MovieDetail(
        id = movie.id,
        title = movie.title,
        releaseDate = movie.releaseDate,
        audienceRating = movie.voteAverage,
        status = movie.status,
        popularity = movie.popularity,
        genres = movie.genres,
        runtime = movie.runtime,
        overview = movie.overview,
        mainBackdrop = Image(movie.mainBackdropPath ?: ""),
        mainPoster = Image(movie.mainPosterPath ?: ""),
        backdrops = images?.filter { it.imageType == ImageEntity.Type.Backdrop }?.toDomains(),
        posters = images?.filter { it.imageType == ImageEntity.Type.Poster }?.toDomains(),
        videos = videos?.toDomains(),
    )
}