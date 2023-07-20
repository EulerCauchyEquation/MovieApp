package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieListRemoteKeyEntity
import com.hwonchul.movie.domain.model.MovieListType

@Dao
interface MovieListRemoteKeyDao : BaseDao<MovieListRemoteKeyEntity> {

    @Query("SELECT * FROM movie_list_remote_key WHERE  listType = :listType ")
    suspend fun getRemoteKeyByListType(listType: MovieListType): MovieListRemoteKeyEntity

    @Query("DELETE FROM movie_list_remote_key WHERE  listType = :listType ")
    suspend fun deleteByListType(listType: MovieListType)

    @Upsert
    suspend fun upsert(entity: MovieListRemoteKeyEntity)
}