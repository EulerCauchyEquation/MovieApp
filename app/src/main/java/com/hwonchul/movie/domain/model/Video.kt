package com.hwonchul.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val title: String
) : Parcelable {
    val videoUrl : String
        get() = "https://youtu.be/$id"

    val thumbnailUrl : String
        get() = "https://img.youtube.com/vi/$id/sddefault.jpg"
}
