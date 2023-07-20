package com.hwonchul.movie.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hwonchul.movie.data.local.model.MovieListRemoteKeyEntity.Companion.TABLE_NAME
import com.hwonchul.movie.domain.model.MovieListType

@Entity(tableName = TABLE_NAME)
data class MovieListRemoteKeyEntity(
    @PrimaryKey
    val listType: MovieListType,
    val nextPageKey: Int? = null,
) {
    companion object {
        const val TABLE_NAME = "movie_list_remote_key"
    }
}