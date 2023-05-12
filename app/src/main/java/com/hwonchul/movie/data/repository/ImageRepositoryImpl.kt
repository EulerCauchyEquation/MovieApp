package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.ImageDao
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.ImageResponse
import com.hwonchul.movie.data.remote.model.toEntities
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.repository.ImageRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val imageDao: ImageDao,
) : ImageRepository {

    override fun getAllPhotosByMovieId(movieId: Int): Flowable<List<Image>> {
        return imageDao.findImagesByMovieId(movieId).map { it.toDomains() }
    }

    override fun refreshFromRemote(movieId: Int): Completable {
        return api.getImageList(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(response: ImageResponse): Completable {
        return Single.just(response)
            .map { it.toEntities() }
            .flatMapCompletable { imageDao.upsert(it) }
    }
}