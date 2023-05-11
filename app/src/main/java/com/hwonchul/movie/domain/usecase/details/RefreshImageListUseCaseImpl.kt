package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.ImageRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RefreshImageListUseCaseImpl @Inject constructor(
    private val repository: ImageRepository
) : RefreshImageListUseCase {

    override fun invoke(movieId: Int): Completable {
        return repository.refreshFromRemote(movieId)
    }
}