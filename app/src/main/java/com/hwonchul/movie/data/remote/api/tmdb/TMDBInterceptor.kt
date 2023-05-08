package com.hwonchul.movie.data.remote.api.tmdb

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.io.IOException

class TMDBInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // response 가로채기
        val response = chain.proceed(chain.request())
        if (!response.isSuccessful) {
            throw IOException("TMDB API : 응답 실패")
        }

        val filteredBody = with(response) {
            // path 별로 원하는 json 으로
            request.url.toUrl().path
                .also { Timber.d("response path : $it") }
                .let { path -> getFilteredJson(path, body!!) }
                .toResponseBody(body!!.contentType())
        }

        return response.newBuilder()
            .body(filteredBody)
            .build()
    }

    private fun getFilteredJson(path: String, body: ResponseBody) = with(path) {
        when {
            //TMDB API Get Videos 호출일 때
            matches(Regex(REGEX_GET_VIDEOS)) -> getBodyToVideoResponse(body).toString()
            // TMDB API Get Popular 호출인 경우
            matches(Regex(REGEX_GET_NOW_PLAYING)) -> getBodyToNowPlayingListResponse(body).toString()
            // TMDB API Get Movie Detail 호출인 경우
            matches(Regex(REGEX_GET_DETAIL)) -> getBodyToMovieDetailResponse(body).toString()
            // 그 외
            else -> body.string()
        }
    }

    private fun getBodyToNowPlayingListResponse(body: ResponseBody): JsonArray {
        return JsonParser.parseString(body.string())
            .asJsonObject
            .getAsJsonArray(KEY_RESULTS)
    }

    private fun getBodyToMovieDetailResponse(body: ResponseBody): JsonObject {
        return JsonParser.parseString(body.string())
            .asJsonObject
            .apply {
                remove(KEY_DETAIL_PRODUCTION_COMPANIES)
                remove(KEY_DETAIL_PRODUCTION_COUNTRIES)
                remove(KEY_DETAIL_SPOKEN_LANGUAGES)
            }
    }

    private fun getBodyToVideoResponse(body: ResponseBody): JsonArray {
        return JsonArray().apply {
            val src = JsonParser.parseString(body.string()).asJsonObject
            val id = src.get(KEY_ID)

            src.getAsJsonArray(KEY_RESULTS)
                .takeUnless { it.isEmpty }
                ?.forEach {
                    it.asJsonObject.apply { add(KEY_ID, id) }
                    add(it)
                }
        }
    }

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_RESULTS = "results"

        private const val KEY_DETAIL_PRODUCTION_COMPANIES = "production_companies"
        private const val KEY_DETAIL_PRODUCTION_COUNTRIES = "production_countries"
        private const val KEY_DETAIL_SPOKEN_LANGUAGES = "spoken_languages"

        private const val REGEX_GET_VIDEOS = "^/3/movie/[0-9]+/videos\$"
        private const val REGEX_GET_NOW_PLAYING = "^/3/movie/now_playing\$"
        private const val REGEX_GET_DETAIL = "^/3/movie/[0-9]+\$"
    }
}