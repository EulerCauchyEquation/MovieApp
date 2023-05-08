package com.hwonchul.movie.data.mapper;

import com.hwonchul.movie.data.local.model.VideoEntity;
import com.hwonchul.movie.data.remote.model.VideoDto;
import com.hwonchul.movie.domain.model.Video;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Mapper
public interface VideoMapper {

    @Named("entityToVideo")
    Video mapEntityToVideo(VideoEntity entity);

    @Mappings({
            @Mapping(target = "id", source = "key"),
            @Mapping(target = "movieId", source = "id"),
            @Mapping(target = "title", source = "name"),
            @Mapping(target = "publishedAt", source = "publishedAt", qualifiedByName = "dateToLocalDate"),
    })
    @Named("dtoToEntity")
    VideoEntity mapDtoToEntity(VideoDto dto);

    @IterableMapping(qualifiedByName = "dtoToEntity")
    List<VideoEntity> mapDtosToEntities(List<VideoDto> dtos);

    @IterableMapping(qualifiedByName = "entityToVideo")
    List<Video> mapEntitiesToVideos(List<VideoEntity> entities);

    @Named("dateToLocalDate")
    default LocalDate dateToLocalDate(String date) {
        return Optional.ofNullable(date)
                .map(val -> ZonedDateTime.parse(val, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz"))
                        .toLocalDate()
                ).orElse(null);
    }
}