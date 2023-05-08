package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("backdrops") val backdrops: List<ImageDto>,
    @SerializedName("posters") val posters: List<ImageDto>,
)
