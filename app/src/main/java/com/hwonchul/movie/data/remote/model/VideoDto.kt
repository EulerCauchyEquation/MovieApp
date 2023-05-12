package com.hwonchul.movie.data.remote.model

import com.google.gson.annotations.SerializedName
import com.hwonchul.movie.data.local.model.VideoEntity
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class VideoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("iso_639_1") val iso6391: String?,
    @SerializedName("iso_3166_1") val iso31661: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String?,
    @SerializedName("size") val size: Int?,
    @SerializedName("type") val type: String,
    @SerializedName("official") val official: Boolean?,
    @SerializedName("published_at") val publishedAt: String?
)

fun VideoDto.toEntity(): VideoEntity {
    val publishedAt = publishedAt?.let {
        ZonedDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"))
            .toLocalDate()
    }
    return VideoEntity(id = key, movieId = id, title = name, publishedAt = publishedAt)
}

fun List<VideoDto>.toEntities(): List<VideoEntity> {
    return this.map { it.toEntity() }
}