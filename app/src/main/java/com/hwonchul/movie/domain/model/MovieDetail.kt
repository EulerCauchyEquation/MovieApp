package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class MovieDetail(
    val id: Int,
    val title: String? = "",
    val releaseDate: LocalDate? = LocalDate.now(),
    val audienceRating: Double? = 0.0,
    val status: String? = "Released",
    val popularity: Double? = 0.0,
    val genres: String? = "",
    val runtime: Int? = 0,
    val overview: String? = "",
    val mainBackdrop: Image? = Image.EMPTY,
    val mainPoster: Image? = Image.EMPTY,
    val backdrops: List<Image>? = listOf(),
    val posters: List<Image>? = listOf(),
    val videos: List<Video>? = listOf(),
) : Parcelable {
    companion object {
        val EMPTY = MovieDetail(id = 0)
    }
}