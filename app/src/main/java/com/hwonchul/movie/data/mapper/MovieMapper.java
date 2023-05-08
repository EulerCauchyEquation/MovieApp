package com.hwonchul.movie.data.mapper;

import com.hwonchul.movie.data.local.model.MovieEntity;
import com.hwonchul.movie.data.local.model.MovieProjection;
import com.hwonchul.movie.data.local.model.MovieWithMedia;
import com.hwonchul.movie.data.remote.model.GenreDto;
import com.hwonchul.movie.data.remote.model.MovieDto;
import com.hwonchul.movie.data.remote.model.MovieProjectionDto;
import com.hwonchul.movie.domain.model.Image;
import com.hwonchul.movie.domain.model.Movie;
import com.hwonchul.movie.domain.model.MovieDetail;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(uses = ImageMapper.class)
public interface MovieMapper {

    @Mappings({
            @Mapping(target = "releaseDate", source = "releaseDate", qualifiedByName = "dateFormat"),
            @Mapping(target = "mainBackdropPath", source = "backdropPath"),
            @Mapping(target = "mainPosterPath", source = "posterPath"),
    })
    @Named("projectionDtoToProjection")
    MovieProjection toMovieProjection(MovieProjectionDto dto);

    @Mappings({
            @Mapping(target = "rating", source = "voteAverage"),
            @Mapping(target = "poster", source = "mainPosterPath", qualifiedByName = "imageMapping"),
            @Mapping(target = "backdrop", source = "mainBackdropPath", qualifiedByName = "imageMapping"),
    })
    @Named("projectionToMovie")
    Movie toMovie(MovieProjection movieProjection);

    @Mappings({
            @Mapping(target = "genres", source = "genres", qualifiedByName = "convertGenreToString"),
            @Mapping(target = "mainBackdropPath", source = "backdropPath"),
            @Mapping(target = "mainPosterPath", source = "posterPath"),
    })
    MovieEntity toEntity(MovieDto dto);

    @IterableMapping(qualifiedByName = "projectionDtoToProjection")
    List<MovieProjection> toMovieProjections(List<MovieProjectionDto> dtos);

    @IterableMapping(qualifiedByName = "projectionToMovie")
    List<Movie> toMovies(List<MovieProjection> projections);

    @Mappings({
            @Mapping(target = "id", source = "movie.id"),
            @Mapping(target = "title", source = "movie.title"),
            @Mapping(target = "releaseDate", source = "movie.releaseDate"),
            @Mapping(target = "audienceRating", source = "movie.voteAverage"),
            @Mapping(target = "status", source = "movie.status"),
            @Mapping(target = "popularity", source = "movie.popularity"),
            @Mapping(target = "genres", source = "movie.genres"),
            @Mapping(target = "runtime", source = "movie.runtime"),
            @Mapping(target = "overview", source = "movie.overview"),
            @Mapping(target = "mainBackdrop", source = "movie.mainBackdropPath", qualifiedByName = "imageMapping"),
            @Mapping(target = "mainPoster", source = "movie.mainPosterPath", qualifiedByName = "imageMapping"),
            @Mapping(target = "backdrops", source = "backdrops"),
            @Mapping(target = "posters", source = "posters"),
            @Mapping(target = "videos", source = "videos"),
    })
    MovieDetail toMovieDetail(MovieWithMedia entity);

    @Named("imageMapping")
    default Image mapImage(String path) {
        return Optional.ofNullable(path)
                .map(Image::new)
                .orElse(null);
    }

    @Named("dateFormat")
    default LocalDate dateFormat(String date) {
        return Optional.ofNullable(date)
                .map(val -> LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .orElse(null);
    }

    @Named("convertGenreToString")
    default String convertGenreToString(List<GenreDto> dtoList) {
        return Optional.ofNullable(dtoList)
                .map(dtos -> dtos.stream()
                        .map(GenreDto::getName)
                        .collect(Collectors.joining(",")))
                .orElse(null);
    }
}
