package com.example.moviesapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.moviesapi.api.entities.Movie;

import java.util.List;
import java.time.Year;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @RestResource(path = "findByGenresId", rel = "findByGenresId")
    List<Movie> findByGenresId(Long genreId);
    @RestResource(path = "findByReleaseYear", rel = "findByReleaseYear")
    List<Movie> findByReleaseYear(Year releaseYear);
    @RestResource(path = "findByActorsId", rel = "findByActorsId")
    List<Movie> findByActorsId(Long actorId);
    @RestResource(path = "findByTitleContains", rel = "findByTitleContains")
    List<Movie> findByTitleContains(String title);

    // Filters without pagination
    @RestResource(path = "findByGenresIdAndReleaseYearAndActorsId", rel = "findByGenresIdAndReleaseYearAndActorsId")
    List<Movie> findByGenresIdAndReleaseYearAndActorsId(Long genreId, Year releaseYear, Long actorId);
    @RestResource(path = "findByGenresIdAndReleaseYear", rel = "findByGenresIdAndReleaseYear")
    List<Movie> findByGenresIdAndReleaseYear(Long genreId, Year releaseYear);
    @RestResource(path = "findByGenresIdAndActorsId", rel = "findByGenresIdAndActorsId")
    List<Movie> findByGenresIdAndActorsId(Long genreId, Long actorId);
    @RestResource(path = "findByReleaseYearAndActorsId", rel = "findByReleaseYearAndActorsId")
    List<Movie> findByReleaseYearAndActorsId(Year releaseYear, Long actorId);

    // Filters with pagination
    @RestResource(path = "findByGenresIdPageable", rel = "findByGenresIdPageable")
    Page<Movie> findByGenresId(Long genreId, Pageable pageable);
    @RestResource(path = "findByReleaseYearPageable", rel = "findByReleaseYearPageable")
    Page<Movie> findByReleaseYear(Year releaseYear, Pageable pageable);
    @RestResource(path = "findByActorsIdPageable", rel = "findByActorsIdPageable")
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);
    @RestResource(path = "findByTitleContainsPageable", rel = "findByTitleContainsPageable")
    Page<Movie> findByTitleContains(String title, Pageable pageable);

    @RestResource(path = "findByGenresIdAndReleaseYearPageable", rel = "findByGenresIdAndReleaseYearPageable")
    Page<Movie> findByGenresIdAndReleaseYear(Long genreId, Year releaseYear, Pageable pageable);
    @RestResource(path = "findByGenresIdAndActorsIdPageable", rel = "findByGenresIdAndActorsIdPageable")
    Page<Movie> findByGenresIdAndActorsId(Long genreId, Long actorId, Pageable pageable);
    @RestResource(path = "findByReleaseYearAndActorsIdPageable", rel = "findByReleaseYearAndActorsIdPageable")
    Page<Movie> findByReleaseYearAndActorsId(Year releaseYear, Long actorId, Pageable pageable);
    @RestResource(path = "findByGenresIdAndReleaseYearAndActorsIdPageable", rel = "findByGenresIdAndReleaseYearAndActorsIdPageable")
    Page<Movie> findByGenresIdAndReleaseYearAndActorsId(Long genreId, Year releaseYear, Long actorId, Pageable pageable);
}