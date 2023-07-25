package com.hwonchul.movie.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao : BaseDao<FavoritesEntity> {

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoritesEntity>>

    @Query("SELECT * FROM favorites WHERE movie_id = :movieId")
    fun getFavoritesByMovieId(movieId: Int): Flow<FavoritesEntity>

    @Upsert
    suspend fun upsert(entity: FavoritesEntity)

    @Upsert
    suspend fun upsert(entities: List<FavoritesEntity>)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}