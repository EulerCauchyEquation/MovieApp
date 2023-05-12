package com.hwonchul.movie.domain.usecase.details

import io.reactivex.rxjava3.core.Completable

/**
 * 영화 동영상(예고편) 새로고침 usecase
 */
interface RefreshVideoListUseCase {

    operator fun invoke(movieId: Int): Completable
}