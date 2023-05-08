package com.hwonchul.movie.data.mapper;

import com.hwonchul.movie.data.local.model.ImageEntity;
import com.hwonchul.movie.data.remote.model.ImageDto;
import com.hwonchul.movie.data.remote.model.ImageResponse;
import com.hwonchul.movie.domain.model.Image;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface ImageMapper {
    @Mappings({
            @Mapping(target = "path", source = "dto.filePath"),
            @Mapping(target = "imageType", source = "type"),
            @Mapping(target = "movieId", source = "movieId"),
    })
    ImageEntity mapDtoToEntity(ImageDto dto, int movieId, ImageEntity.Type type);

    default List<ImageEntity> mapResponseToEntities(ImageResponse response) {
        final Stream<ImageEntity> backdrops = response.getBackdrops().stream()
                .map(imageDto -> mapDtoToEntity(imageDto, response.getId(), ImageEntity.Type.Backdrop));

        final Stream<ImageEntity> posters = response.getPosters().stream()
                .map(imageDto -> mapDtoToEntity(imageDto, response.getId(), ImageEntity.Type.Poster));

        return Stream.concat(backdrops, posters).collect(Collectors.toList());
    }

    @Named("entityToImage")
    Image mapEntityToImage(ImageEntity entity);

    @IterableMapping(qualifiedByName = "entityToImage")
    List<Image> mapEntitiesToImages(List<ImageEntity> entities);
}
