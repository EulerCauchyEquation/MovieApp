package com.hwonchul.movie.data.repository

import com.hwonchul.movie.data.local.dao.VideoDao
import com.hwonchul.movie.data.local.model.toDomains
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi
import com.hwonchul.movie.data.remote.model.toEntity
import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val videoDao: VideoDao,
) : VideoRepository {

    override fun getAllVideosByMovieId(movieId: Int): Flow<List<Video>> {
        return videoDao.findVideosByMovieId(movieId).map { it.toDomains() }
    }

    override suspend fun refreshFromRemote(movieId: Int) {
        val entities = api.getVideoList(movieId).map { it.toEntity() }
        videoDao.upsert(entities)
    }
}