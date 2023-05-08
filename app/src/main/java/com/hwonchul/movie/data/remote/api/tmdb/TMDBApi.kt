package com.hwonchul.movie.data.remote.api.tmdb

import com.hwonchul.movie.data.remote.model.*
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class TMDBApi @Inject constructor(private val service: TMDBService) {

    fun getNowPlayingMovieList(): Single<List<MovieProjectionDto>> {
        return service.getNowPlayingList()
    }

    fun getMovie(movieId: Int): Single<MovieDto> {
        return service.getMovie(movieId)
    }

    fun getVideoList(movieId: Int): Single<List<VideoDto>> {
        return service.getVideoList(movieId)
    }

    fun getImageList(movieId: Int): Single<ImageResponse> {
        return service.getImageList(movieId)
    }
}