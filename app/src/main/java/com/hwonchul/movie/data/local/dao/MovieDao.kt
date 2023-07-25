package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.MovieEntity
import com.hwonchul.movie.data.local.model.MovieWithFavorites
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MovieDao : BaseDao<MovieEntity> {

    @Query(
        "SELECT movie.*, favorites.movie_id IS NOT NULL as favorites " +
                "FROM movie " +
                "LEFT JOIN favorites ON movie.id = favorites.movie_id " +
                "WHERE release_date > :today " +
                "ORDER BY popularity DESC "
    )
    fun findAllUnreleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): Flow<List<MovieWithFavorites>>

    @Query(
        "SELECT movie.*, favorites.movie_id IS NOT NULL as favorites " +
                "FROM movie " +
                "LEFT JOIN favorites ON movie.id = favorites.movie_id " +
                "WHERE release_date <= :today " +
                "ORDER BY popularity DESC "
    )
    fun findAllReleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): Flow<List<MovieWithFavorites>>

    @Query(
        "SELECT movie.*, favorites.movie_id IS NOT NULL as favorites " +
                "FROM movie " +
                "LEFT JOIN favorites ON movie.id = favorites.movie_id " +
                "WHERE release_date > :today " +
                "ORDER BY popularity DESC "
    )
    fun findAllPagedUnreleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): PagingSource<Int, MovieWithFavorites>

    @Query(
        "SELECT movie.*, favorites.movie_id IS NOT NULL as favorites " +
                "FROM movie " +
                "LEFT JOIN favorites ON movie.id = favorites.movie_id " +
                "WHERE release_date <= :today " +
                "ORDER BY popularity DESC "
    )
    fun findAllPagedReleasedMoviesOrderByPopularity(today: LocalDate = LocalDate.now()): PagingSource<Int, MovieWithFavorites>

    @Query(
        "SELECT movie.*, favorites.movie_id IS NOT NULL as favorites " +
                "FROM movie " +
                "LEFT JOIN favorites ON movie.id = favorites.movie_id " +
                "WHERE keyword = :keyword " +
                "ORDER BY popularity DESC"
    )
    fun findAllPagedMoviesByKeyword(keyword: String): PagingSource<Int, MovieWithFavorites>

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

    @Query("DELETE FROM ${MovieEntity.TABLE_NAME} WHERE keyword = :keyword")
    suspend fun deleteByKeyword(keyword: String)

    @Query("DELETE FROM ${MovieEntity.TABLE_NAME} WHERE release_date <= :today")
    suspend fun deleteByReleased(today: LocalDate = LocalDate.now())

    @Query("DELETE FROM ${MovieEntity.TABLE_NAME} WHERE release_date > :today")
    suspend fun deleteByUnreleased(today: LocalDate = LocalDate.now())
}