package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hwonchul.movie.domain.model.Image

@Entity(
    tableName = ImageEntity.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = ["id"],
        childColumns = ["movie_id"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class ImageEntity(
    @PrimaryKey
    @ColumnInfo(name = "path")
    val path: String,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "image_type") val imageType: Type,
) {
    companion object {
        const val TABLE_NAME = "image"
    }

    enum class Type {
        Poster, Backdrop
    }
}

fun ImageEntity.toDomains(): Image {
    return Image(path = path)
}

fun List<ImageEntity>.toDomains(): List<Image> {
    return this.map { it.toDomains() }
}