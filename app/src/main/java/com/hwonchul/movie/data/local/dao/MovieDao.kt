package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MovieDao : BaseDao<MovieEntity> {

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE release_date > :today ORDER BY popularity DESC")
    fun findAllUnreleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): Flow<List<MovieEntity>>

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE release_date <= :today ORDER BY popularity DESC")
    fun findAllReleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): Flow<List<MovieEntity>>

    @Query("SELECT * FROM ${MovieEntity.TABLE_NAME} WHERE id =:id")
    fun findMovieById(id: Int): Flow<List<MovieEntity>>

    suspend fun upsert(entity: MovieEntity) {
        try {
            insert(entity)
        } catch (e: SQLiteConstraintException) {
            update(entity)
        }
    }

    suspend fun upsert(entities: List<MovieEntity>) {
        entities.forEach { entity ->
            try {
                insert(entity)
            } catch (e: SQLiteConstraintException) {
                update(entity)
            }
        }
    }

    @Query("DELETE FROM ${MovieEntity.TABLE_NAME}")
    suspend fun deleteAll()
}