package com.hwonchul.movie.data.repository;

import com.hwonchul.movie.data.local.dao.ImageDao;
import com.hwonchul.movie.data.mapper.ImageMapper;
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi;
import com.hwonchul.movie.data.remote.model.ImageResponse;
import com.hwonchul.movie.domain.model.Image;
import com.hwonchul.movie.domain.repository.ImageRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ImageRepositoryImpl implements ImageRepository {
    private final TMDBApi api;
    private final ImageDao imageDao;
    private final ImageMapper mapper;

    @Inject
    public ImageRepositoryImpl(@NonNull TMDBApi api,
                               @NonNull ImageDao imageDao,
                               @NonNull ImageMapper mapper) {
        this.api = api;
        this.imageDao = imageDao;
        this.mapper = mapper;
    }

    @Override
    public Flowable<List<Image>> getAllPhotosByMovieId(int movieId) {
        // local 에서 데이터 불러오기
        return imageDao.findImagesByMovieId(movieId).map(mapper::mapEntitiesToImages);
    }

    @Override
    public Completable refreshFromRemote(int movieId) {
        // API 에서 받은 최신 데이터를 local 에 저장
        return api.getImageList(movieId).flatMapCompletable(this::saveToLocal);
    }

    private Completable saveToLocal(ImageResponse response) {
        return Single.just(response)
                .map(mapper::mapResponseToEntities)
                .flatMapCompletable(imageDao::insert);
    }
}
