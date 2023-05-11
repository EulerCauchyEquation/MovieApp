package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.repository.VideoRepository
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RefreshVideoListUseCaseImpl @Inject constructor(
    private val repository: VideoRepository
) : RefreshVideoListUseCase {

    override fun invoke(movieId: Int): Completable {
        return repository.refreshFromRemote(movieId)
    }
}