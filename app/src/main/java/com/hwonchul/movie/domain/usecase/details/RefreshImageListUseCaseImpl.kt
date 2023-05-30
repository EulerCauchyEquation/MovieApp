package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.ImageRepository
import javax.inject.Inject

class RefreshImageListUseCaseImpl @Inject constructor(
    private val repository: ImageRepository
) : RefreshImageListUseCase {

    override suspend fun invoke(movieId: Int): Result<Unit> =
        runCatching { repository.refreshFromRemote(movieId) }
}