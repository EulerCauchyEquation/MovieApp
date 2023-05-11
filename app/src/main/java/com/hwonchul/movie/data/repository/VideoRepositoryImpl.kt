package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.VideoDao
import com.hwonchul.movie.data.mapper.VideoMapper
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.VideoDto
import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.repository.VideoRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val videoDao: VideoDao,
    private val mapper: VideoMapper
) : VideoRepository {

    override fun getAllVideosByMovieId(movieId: Int): Flowable<List<Video>> {
        // local 에서 데이터 가져온다
        return videoDao.findVideosByMovieId(movieId).map { mapper.mapEntitiesToVideos(it) }
    }

    override fun refreshFromRemote(movieId: Int): Completable {
        // API 에서 받은 최신 데이터를 local 에 저장
        return api.getVideoList(movieId).flatMapCompletable { saveToLocal(it) }
    }

    private fun saveToLocal(dtos: List<VideoDto>): Completable {
        return Single.just(dtos)
            .map { mapper.mapDtosToEntities(it) }
            .flatMapCompletable { videoDao.insert(it) }
    }
}