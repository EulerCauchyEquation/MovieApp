package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val path: String,
) : Parcelable {
    val url: String
        get() = "https://image.tmdb.org/t/p/w500$path"

    companion object {
        val EMPTY = Image(path = "")
    }
}

