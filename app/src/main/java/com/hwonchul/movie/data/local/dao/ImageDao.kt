package com.hwonchul.movie.data.local.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Query
import com.hwonchul.movie.base.data.dao.BaseDao
import com.hwonchul.movie.data.local.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao : BaseDao<ImageEntity> {

    @Query("SELECT * FROM " + ImageEntity.TABLE_NAME + " WHERE movie_id = :movieId")
    fun findImagesByMovieId(movieId: Int): Flow<List<ImageEntity>>

    suspend fun upsert(entity: ImageEntity) {
        try {
            insert(entity)
        } catch (e: SQLiteConstraintException) {
            update(entity)
        }
    }

    suspend fun upsert(entities: List<ImageEntity>) {
        entities.forEach { entity ->
            try {
                insert(entity)
            } catch (e: SQLiteConstraintException) {
                update(entity)
            }
        }
    }
}