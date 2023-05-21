package com.hwonchul.movie.domain.usecase.listing

import com.hwonchul.movie.domain.model.Movie
import com.hwonchul.movie.domain.model.MovieListType
import com.hwonchul.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    private val repository: MovieRepository
) : GetMovieListUseCase {

    override suspend fun invoke(listType: MovieListType): Flow<Result<List<Movie>>> {
        return repository.getAllMoviesByListType(listType)
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}