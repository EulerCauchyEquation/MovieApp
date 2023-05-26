package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.ImageDao
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.toEntities
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val imageDao: ImageDao,
) : ImageRepository {

    override fun getAllPhotosByMovieId(movieId: Int): Flow<List<Image>> {
        return imageDao.findImagesByMovieId(movieId).map { it.toDomains() }
    }

    override suspend fun refreshFromRemote(movieId: Int) {
        val entities = api.getImageList(movieId).toEntities()
        imageDao.upsert(entities)
    }
}