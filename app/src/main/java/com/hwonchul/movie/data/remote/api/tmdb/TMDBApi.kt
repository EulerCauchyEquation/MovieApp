package com.hwonchul.movie.data.remote.api.tmdb

import com.hwonchul.movie.data.remote.model.ImageResponse
import com.hwonchul.movie.data.remote.model.MovieDto
import com.hwonchul.movie.data.remote.model.MovieProjectionDto
import com.hwonchul.movie.data.remote.model.VideoDto
import javax.inject.Inject

class TMDBApi @Inject constructor(private val service: TMDBService) {

    suspend fun getNowPlayingMovieList(): List<MovieProjectionDto> {
        return service.getNowPlayingList()
    }

    suspend fun getMovie(movieId: Int): MovieDto {
        return service.getMovie(movieId)
    }

    suspend fun getVideoList(movieId: Int): List<VideoDto> {
        return service.getVideoList(movieId)
    }

    suspend fun getImageList(movieId: Int): ImageResponse {
        return service.getImageList(movieId)
    }
}