package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.ImageDao
import com.hwonchul.movie.data.mapper.ImageMapper
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.ImageResponse
import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.repository.ImageRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val imageDao: ImageDao,
    private val mapper: ImageMapper
) : ImageRepository {

    override fun getAllPhotosByMovieId(movieId: Int): Flowable<List<Image>> {
        // local 에서 데이터 불러오기
        return imageDao.findImagesByMovieId(movieId).map { mapper.mapEntitiesToImages(it) }
    }

    override fun refreshFromRemote(movieId: Int): Completable {
        // API 에서 받은 최신 데이터를 local 에 저장
        return api.getImageList(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(response: ImageResponse): Completable {
        return Single.just(response)
            .map { mapper.mapResponseToEntities(it) }
            .flatMapCompletable { imageDao.insert(it) }
    }
}