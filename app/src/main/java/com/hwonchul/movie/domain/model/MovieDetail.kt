package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class MovieDetail(
    val id: Int,
    val title: String?,
    val releaseDate: LocalDate?,
    val audienceRating: Double?,
    val status: String?,
    val popularity: Double?,
    val genres: String?,
    val runtime: Int?,
    val overview: String?,
    val mainBackdrop: Image?,
    val mainPoster: Image?,
    val backdrops: List<Image>?,
    val posters: List<Image>?,
    val videos: List<Video>?,
) : Parcelable