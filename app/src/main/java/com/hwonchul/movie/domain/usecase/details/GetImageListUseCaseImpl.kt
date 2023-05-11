package com.hwonchul.movie.domain.usecase.details

import com.hwonchul.movie.domain.model.Image
import com.hwonchul.movie.domain.repository.ImageRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class GetImageListUseCaseImpl @Inject constructor(
    private val repository: ImageRepository
) : GetImageListUseCase {

    override fun invoke(movieId: Int): Flowable<List<Image>> {
        return repository.getAllPhotosByMovieId(movieId)
    }
}