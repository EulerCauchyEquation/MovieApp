package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.hwonchul.movie.data.local.converter.LocalDateConverter
import java.time.LocalDate

@Entity(tableName = MovieDetailEntity.TABLE_NAME)
data class MovieDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @TypeConverters(LocalDateConverter::class)
    @ColumnInfo(name = "release_date")
    val releaseDate: LocalDate?,
    @ColumnInfo(name = "popularity") val popularity: Double,
    @ColumnInfo(name = "vote_average") val voteAverage: Double,
    @ColumnInfo(name = "runtime") val runtime: Int?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "genre") val genres: String?,
) {
    companion object {
        const val TABLE_NAME = "movie_detail"
    }
}