package com.hwonchul.movie.data.remote.api.tmdb

import com.hwonchul.movie.data.remote.model.ImageResponse
import com.hwonchul.movie.data.remote.model.MovieDetailDto
import com.hwonchul.movie.data.remote.model.MovieDto
import com.hwonchul.movie.data.remote.model.VideoDto
import com.hwonchul.movie.domain.model.MovieListType
import javax.inject.Inject

class TMDBApi @Inject constructor(private val service: TMDBService) {

    suspend fun getMovieList(
        type: MovieListType,
        page: Int = TMDBService.INITIAL_PAGE_INDEX
    ): List<MovieDto> {
        val response = when (type) {
            MovieListType.NowPlaying -> service.getNowPlayingList(page = page)
            MovieListType.UpComing -> service.getUpComingList(page = page)
        }
        return response.results
    }

    suspend fun getMovie(movieId: Int): MovieDetailDto {
        return service.getMovie(movieId)
    }

    suspend fun getVideoList(movieId: Int): List<VideoDto> {
        return service.getVideoList(movieId)
    }

    suspend fun getImageList(movieId: Int): ImageResponse {
        return service.getImageList(movieId)
    }

    suspend fun searchMovieByKeyword(
        keyword: String,
        page: Int = TMDBService.INITIAL_PAGE_INDEX
    ): List<MovieDto> {
        return service.searchMoviesByKeyword(query = keyword, page = page).results
    }
}