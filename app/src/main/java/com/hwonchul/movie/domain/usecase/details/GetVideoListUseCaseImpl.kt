package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Video
import com.hwonchul.movie.domain.repository.VideoRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val repository: VideoRepository
) : GetVideoListUseCase {

    override fun invoke(movieId: Int): Flowable<List<Video>> {
        return repository.getAllVideosByMovieId(movieId)
    }
}