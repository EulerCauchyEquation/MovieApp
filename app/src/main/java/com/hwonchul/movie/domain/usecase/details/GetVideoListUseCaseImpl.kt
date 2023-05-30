package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val repository: VideoRepository
) : GetVideoListUseCase {

    override fun invoke(movieId: Int): Flow<Result<List<Video>>> {
        return repository.getAllVideosByMovieId(movieId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}