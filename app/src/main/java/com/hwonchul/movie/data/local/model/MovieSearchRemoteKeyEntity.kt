package com.hwonchul.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hwonchul.movie.data.local.model.MovieSearchRemoteKeyEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class MovieSearchRemoteKeyEntity(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)  // 대소문자 구문 x
    val keyword: String,
    val nextPageKey: Int? = null,
) {
    companion object {
        const val TABLE_NAME = "movie_search_remote_key"
    }
}