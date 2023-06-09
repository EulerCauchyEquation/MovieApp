package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import com.hwonchul.movie.domain.model.Video
import java.time.LocalDate

@Entity(
    tableName = VideoEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = MovieDetailEntity::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class VideoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "title") val title: String?,
    @TypeConverters(LocalDateConverter::class)
    @ColumnInfo(name = "published_at")
    val publishedAt: LocalDate?,
) {
    companion object {
        const val TABLE_NAME = "video"
    }
}

fun VideoEntity.toDomain(): Video {
    return Video(id = id, title = title ?: "")
}

fun List<VideoEntity>.toDomains(): List<Video> {
    return this.map { it.toDomain() }
}