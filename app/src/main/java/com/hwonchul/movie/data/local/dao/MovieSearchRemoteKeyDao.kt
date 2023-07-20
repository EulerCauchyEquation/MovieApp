package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieSearchRemoteKeyEntity

@Dao
interface MovieSearchRemoteKeyDao : BaseDao<MovieSearchRemoteKeyEntity> {

    @Query("SELECT * FROM movie_search_remote_key WHERE  keyword = :keyword ")
    suspend fun getRemoteKeyByKeyword(keyword: String): MovieSearchRemoteKeyEntity

    @Query("DELETE FROM movie_search_remote_key WHERE  keyword = :keyword ")
    suspend fun deleteByKeyword(keyword: String)

    @Upsert
    suspend fun upsert(entity : MovieSearchRemoteKeyEntity)
}