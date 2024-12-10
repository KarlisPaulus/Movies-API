package com.example.moviesapi.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.stream.Collectors;

import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;

import com.example.moviesapi.api.entities.Genre;
import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.service.GenreService;
import com.example.moviesapi.service.MovieService;

@Validated
@RestController
@RequestMapping("/api")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private PagedResourcesAssembler<Genre> pagedResourcesAssembler;

    @GetMapping("/genres")
    public ResponseEntity<?> getAllGenres(
            @PositiveOrZero(message = "Page number must be positive or zero.") @RequestParam(required = false) Integer page,
            @Positive(message = "Size must be a positive number") @RequestParam(required = false) Integer size) {

        // Apply pagination if page and size are provided
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Genre> genresPage = genreService.getAllPageableGenres(pageable);
            PagedModel<EntityModel<Genre>> pagedModel = pagedResourcesAssembler.toModel(genresPage);
            return ResponseEntity.ok().body(pagedModel);
        }

        List<Genre> genres = genreService.getAllGenres();

        if (genres.isEmpty()) {
            return ResponseEntity.ok().body("No genres found.");
        }

        return ResponseEntity.ok().body(genres);
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable Long id) {
        Optional<Genre> genre = genreService.getGenreById(id);
        if (!genre.isPresent()) {
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }
        return ResponseEntity.ok().body(genre.get());
    }

    @GetMapping("/genres/{id}/movies")
    public ResponseEntity<?> getGenreAndAssociatedMovies(@PathVariable Long id) {
        Optional<Genre> genreOptional = genreService.getGenreById(id);
        if (!genreOptional.isPresent()) {
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }

        Genre genre = genreOptional.get();

        // Map movies to a simplified structure
        List<Map<String, Object>> associatedMovies = genre.getMovies().stream()
                .map(movie -> {
                    Map<String, Object> movieDetails = new HashMap<>();
                    movieDetails.put("movie_id", movie.getId());
                    movieDetails.put("movie_title", movie.getTitle());
                    return movieDetails;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("genre_id", genre.getId());
        response.put("genre_name", genre.getName());
        response.put("associated_movies", associatedMovies);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/genres")
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        Genre savedGenre = genreService.saveGenre(genre);
        return ResponseEntity.status(201).body(savedGenre);
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<?> deleteGenre(
            @PathVariable Long id,
            @RequestParam(value = "cascade", required = false, defaultValue = "false") boolean cascade) {

        if (!genreService.existsById(id)) {
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }

        Genre genre = genreService.getGenreById(id).get();
        int associatedMoviesCount = genre.getMovies().size();
        String genreName = genre.getName();

        if (associatedMoviesCount > 0) {
            if (cascade) {
                // Delete all associated movies
                for (Movie movie : genre.getMovies()) {
                    movieService.deleteMovie(movie.getId());
                }
                genreService.deleteGenre(id);
                String successMessage = "Genre '" + genreName + "' and its " + associatedMoviesCount
                        + " associated movies have been deleted.";
                return ResponseEntity.status(204).body(successMessage);
            } else {
                String errorMessage = "Cannot delete genre '" + genreName
                        + "' because it has " + associatedMoviesCount + " associated movie(s).";
                return ResponseEntity.status(400).body(errorMessage);
            }
        }

        genreService.deleteGenre(id);
        String successMessage = "Genre '" + genreName + "' has been deleted successfully.";
        return ResponseEntity.status(204).body(successMessage);
    }

    @PatchMapping("/genres/{id}")
    public ResponseEntity<Genre> updateGenreName(
            @PathVariable Long id,
            @Valid @RequestBody Genre genre) {

        if (!genreService.existsById(id)) {
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }

        Genre updatedGenre = genreService.updateGenre(id, genre.getName());
        return ResponseEntity.ok(updatedGenre);
    }
}
