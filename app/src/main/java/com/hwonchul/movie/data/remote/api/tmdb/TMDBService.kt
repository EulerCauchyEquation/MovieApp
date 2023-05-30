package com.hwonchul.movie.data.remote.api.tmdb

import com.hwonchul.movie.BuildConfig
import com.hwonchul.movie.data.remote.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TMDB open API
 */
interface TMDBService {

    @GET(URI_GET_NOW_PLAYING_LIST)
    suspend fun getNowPlayingList(
        @Query(QUERY_API_KEY) key: String = API_KEY,
        @Query(QUERY_LANGUAGE) language: String = LANGUAGE_KO,
        @Query(QUERY_REGION) region: String = REGION_KR,
    ): List<MovieProjectionDto>

    @GET(URI_GET_MOVIE)
    suspend fun getMovie(
        @Path(PATH_MOVIE_ID) movieId: Int,
        @Query(QUERY_API_KEY) key: String = API_KEY,
        @Query(QUERY_LANGUAGE) language: String = LANGUAGE_KO,
    ): MovieDto

    @GET(URI_GET_VIDEOS)
    suspend fun getVideoList(
        @Path(PATH_MOVIE_ID) id: Int,
        @Query(QUERY_API_KEY) key: String = API_KEY,
        @Query(QUERY_LANGUAGE) language: String = LANGUAGE_KO,
    ): List<VideoDto>

    @GET(URI_GET_IMAGES)
    suspend fun getImageList(
        @Path(PATH_MOVIE_ID) id: Int,
        @Query(QUERY_API_KEY) key: String = API_KEY,
    ): ImageResponse

    companion object {
        const val URL = "https://api.themoviedb.org"

        private const val URI_GET_NOW_PLAYING_LIST = "/3/movie/now_playing"
        private const val URI_GET_MOVIE = "/3/movie/{movie_id}"
        private const val URI_GET_VIDEOS = "/3/movie/{movie_id}/videos"
        private const val URI_GET_IMAGES = "/3/movie/{movie_id}/images"

        private const val PATH_MOVIE_ID = "movie_id"

        private const val QUERY_LANGUAGE = "language"
        private const val QUERY_API_KEY = "api_key"
        private const val QUERY_REGION = "region"

        private const val API_KEY = BuildConfig.TMDB_API_KEY
        private const val LANGUAGE_KO = "ko-KR"
        private const val REGION_KR = "KR"
    }
}