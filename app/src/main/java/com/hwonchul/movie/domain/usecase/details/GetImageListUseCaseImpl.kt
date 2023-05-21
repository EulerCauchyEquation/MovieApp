package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImageListUseCaseImpl @Inject constructor(
    private val repository: ImageRepository
) : GetImageListUseCase {

    override suspend fun invoke(movieId: Int): Flow<Result<List<Image>>> =
        repository.getAllPhotosByMovieId(movieId)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}