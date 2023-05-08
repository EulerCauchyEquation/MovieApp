package com.hwonchul.movie.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

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