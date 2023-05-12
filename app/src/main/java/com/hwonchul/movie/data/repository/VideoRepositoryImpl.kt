package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.VideoDao
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.VideoDto
import com.hwonchul.movie.data.remote.model.toEntities
import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.repository.VideoRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val videoDao: VideoDao,
) : VideoRepository {

    override fun getAllVideosByMovieId(movieId: Int): Flowable<List<Video>> {
        return videoDao.findVideosByMovieId(movieId).map { it.toDomains() }
    }

    override fun refreshFromRemote(movieId: Int): Completable {
        return api.getVideoList(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(dtos: List<VideoDto>): Completable {
        return Single.just(dtos)
            .map { it.toEntities() }
            .flatMapCompletable { videoDao.upsert(it) }
    }
}