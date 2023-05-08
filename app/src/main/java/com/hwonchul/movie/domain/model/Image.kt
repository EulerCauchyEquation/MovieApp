package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val path: String,
) : Parcelable {
    val url: String
        get() = "https://image.tmdb.org/t/p/original$path"
}

