package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName
import com.hwonchul.movie.data.local.model.ImageEntity

data class ImageResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("backdrops") val backdrops: List<ImageDto>,
    @SerializedName("posters") val posters: List<ImageDto>,
)

data class ImageDto(
    @SerializedName("aspect_ratio") val aspectRatio: Float,
    @SerializedName("height") val height: Int,
    @SerializedName("iso_639_1") val iso6391: String?,
    @SerializedName("file_path") val filePath: String,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("width") val width: Int
)

fun ImageDto.toEntity(movieId: Int, type: ImageEntity.Type): ImageEntity {
    return ImageEntity(path = filePath, movieId = movieId, imageType = type)
}

fun ImageResponse.toEntities(): List<ImageEntity> {
    val backdrops = backdrops.map { it.toEntity(id, ImageEntity.Type.Backdrop) }
    val posters = posters.map { it.toEntity(id, ImageEntity.Type.Poster) }
    return backdrops + posters
}