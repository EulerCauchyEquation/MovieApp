package com.hwonchul.movie.data.repository;

import androidx.annotation.NonNull;

import com.hwonchul.movie.data.local.dao.VideoDao;
import com.hwonchul.movie.data.mapper.VideoMapper;
import com.hwonchul.movie.data.remote.api.tmdb.TMDBApi;
import com.hwonchul.movie.data.remote.model.VideoDto;
import com.hwonchul.movie.domain.model.Video;
import com.hwonchul.movie.domain.repository.VideoRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class VideoRepositoryImpl implements VideoRepository {
    private final TMDBApi api;
    private final VideoDao videoDao;
    private final VideoMapper mapper;

    @Inject
    public VideoRepositoryImpl(@NonNull TMDBApi api,
                               @NonNull VideoDao videoDao,
                               @NonNull VideoMapper mapper) {
        this.api = api;
        this.videoDao = videoDao;
        this.mapper = mapper;
    }

    public Flowable<List<Video>> getAllVideosByMovieId(int movieId) {
        // local 에서 데이터 가져온다
        return videoDao.findVideosByMovieId(movieId).map(mapper::mapEntitiesToVideos);
    }

    @Override
    public Completable refreshFromRemote(int movieId) {
        // API 에서 받은 최신 데이터를 local 에 저장
        return api.getVideoList(movieId).flatMapCompletable(this::saveToLocal);
    }

    private Completable saveToLocal(List<VideoDto> dtos) {
        return Single.just(dtos)
                .map(mapper::mapDtosToEntities)
                .flatMapCompletable(videoDao::insert);
    }
}
