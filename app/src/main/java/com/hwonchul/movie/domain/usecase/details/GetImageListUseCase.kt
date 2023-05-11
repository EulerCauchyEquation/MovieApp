package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Image
import io.reactivex.rxjava3.core.Flowable

/**
 * 영화 스틸컷 가져오기 Usecase
 */
interface GetImageListUseCase {

    operator fun invoke(movieId: Int): Flowable<List<Image>>
}