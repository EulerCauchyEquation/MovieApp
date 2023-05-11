package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Video
import io.reactivex.rxjava3.core.Flowable

/**
 * 영화 예고편 가져오기 UseCase
 */
interface GetVideoListUseCase {

    operator fun invoke(movieId: Int): Flowable<List<Video>>
}