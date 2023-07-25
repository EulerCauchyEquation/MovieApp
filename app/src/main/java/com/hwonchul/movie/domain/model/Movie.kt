package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Movie(
    val id: Int,
    val title: String?,
    val originalTitle: String?,
    val releaseDate: LocalDate?,
    val rating: Double,
    val popularity: Double,
    val backdrop: Image?,
    val poster: Image?,
    val isFavorite: Boolean = false,
) : Parcelable