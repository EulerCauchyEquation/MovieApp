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
    // 세로 포스터 리스트
    val posters: List<ImageEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id",
    )
    // 가로 포스터 리스트
    val backdrops: List<ImageEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "movie_id",
    )
    // 영화 클립 리스트
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
        backdrops = backdrops?.toDomains(),
        posters = posters?.toDomains(),
        videos = videos?.toDomains(),
    )
}