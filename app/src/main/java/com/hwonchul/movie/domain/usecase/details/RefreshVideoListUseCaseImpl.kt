package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.VideoRepository
import javax.inject.Inject

class RefreshVideoListUseCaseImpl @Inject constructor(
    private val repository: VideoRepository
) : RefreshVideoListUseCase {

    override suspend fun invoke(movieId: Int): Result<Unit> =
        runCatching { repository.refreshFromRemote(movieId) }
}