package com.hwonchul.movie.data.remote.api.tmdb

import com.google.gson.JsonParser
import com.hwonchul.movie.BuildConfig
import com.hwonchul.movie.FileReader
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class TmdbInteceptorTest : BehaviorSpec({
    val interceptor = TMDBInterceptor()
    val chain: Interceptor.Chain = mockk(relaxed = true)

    given("응답 성공, path : \"/3/movie/now_playing\" 일 때") {
        val url =
            TMDBService.URL + "/3/movie/now_playing?api_key=${BuildConfig.TMDB_API_KEY}&language=ko"

        `when`("정상적인 result 면") {
            val successResponseBody =
                successJsonStringWithPopularPath.toResponseBody("application/json;charset=UTF-8".toMediaType())
            val successResponse = tempSuccessResponse.newBuilder().body(successResponseBody)
                .request(Request.Builder().url(url).build()).build()
            every { chain.proceed(any()) } returns successResponse

            val actualResponse = interceptor.intercept(chain)
            then("변형된 Json 으로 들어온다") {
                assertEquals(
                    toJson(expectedJsonStringWithPopularPath),
                    toJson(actualResponse.body!!.string())
                )
            }
        }
    }

    given("응답 성공, path : \"/3/movie/{movie_id}/videos\" 일 때") {
        val url = TMDBService.URL + "/3/movie/478187/videos?api_key=${BuildConfig.TMDB_API_KEY}"

        `when`("empty result 면") {
            val successResponseBody =
                emptySuccessJsonStringWithVideosPath.toResponseBody("application/json;charset=UTF-8".toMediaType())
            val successResponse = tempSuccessResponse.newBuilder().body(successResponseBody)
                .request(Request.Builder().url(url).build()).build()
            every { chain.proceed(any()) } returns successResponse

            val actualResponse = interceptor.intercept(chain)
            then("빈 JsonArray 가 반환된다") {
                assertEquals(
                    toJson(expectedEmptyJsonArrayString), toJson(actualResponse.body!!.string())
                )
            }
        }

        `when`("정상적인 result 면") {
            val successResponseBody =
                successJsonStringWithVideosPath.toResponseBody("application/json;charset=UTF-8".toMediaType())
            val successResponse = tempSuccessResponse.newBuilder().body(successResponseBody)
                .request(Request.Builder().url(url).build()).build()
            every { chain.proceed(any()) } returns successResponse

            val actualResponse = interceptor.intercept(chain)
            then("변형된 Json 으로 들어온다") {
                assertEquals(
                    toJson(expectedJsonStringWithVideosPath), toJson(actualResponse.body!!.string())
                )
            }
        }
    }

    given("응답 실패 시") {
        val errorResponse = Response.Builder().code(404).protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(TMDBService.URL).build()).message("fail").build()

        `when`("intercept 하면") {
            every { chain.proceed(any()) } returns errorResponse
            then("IOException 이 발생한다") {
                assertThrows<IOException> { interceptor.intercept(chain) }
            }
        }
    }
}) {
    companion object {
        val tempSuccessResponse = Response.Builder().code(200).protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(TMDBService.URL).build()).message("ok").build()

        private const val expectedEmptyJsonString = "{}"

        private const val expectedEmptyJsonArrayString = "[]"

        private val emptySuccessJsonStringWithVideosPath =
            FileReader.readStringFromFile("text_data/tmdb/videos/empty_result.json").trimIndent()

        private val successJsonStringWithPopularPath =
            FileReader.readStringFromFile("text_data/tmdb/movieList/nowPlaying/success.json").trimIndent()

        private val successJsonStringWithVideosPath =
            FileReader.readStringFromFile("text_data/tmdb/videos/success.json").trimIndent()

        private val expectedJsonStringWithPopularPath =
            FileReader.readStringFromFile("text_data/tmdb/movieList/nowPlaying/success_filtered.json")
                .trimIndent()

        private val expectedJsonStringWithVideosPath =
            FileReader.readStringFromFile("text_data/tmdb/videos/success_filtered.json")
                .trimIndent()
    }
}

private fun toJson(jsonString: String) = JsonParser.parseString(jsonString)