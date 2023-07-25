package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hwonchul.movie.data.local.model.FavoritesEntity.Companion.TABLE_NAME
import com.hwonchul.movie.domain.model.Favorites

@Entity(tableName = TABLE_NAME)
data class FavoritesEntity(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "user_id") val userId: String,
) {
    companion object {
        const val TABLE_NAME = "favorites"
    }
}

fun FavoritesEntity.toDomain() = Favorites(
    movieId = movieId,
    userId = userId,
    isFavorites = true,
)

fun List<FavoritesEntity>.toDomains() =
    this.map { it.toDomain() }