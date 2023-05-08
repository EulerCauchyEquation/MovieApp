package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("aspect_ratio") val aspectRatio: Float,
    @SerializedName("height") val height: Int,
    @SerializedName("iso_639_1") val iso6391: String?,
    @SerializedName("file_path") val filePath: String?,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("width") val width: Int
)