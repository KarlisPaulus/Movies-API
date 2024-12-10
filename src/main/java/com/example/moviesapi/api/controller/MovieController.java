package com.example.moviesapi.api.controller;

import com.example.moviesapi.api.entities.Genre;
import com.example.moviesapi.api.entities.Actor;
import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.service.GenreService;
import com.example.moviesapi.service.MovieService;
import com.example.moviesapi.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Validated
@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private ActorService actorService;

    @GetMapping("/movies")
    public ResponseEntity<?> getMoviesByFilter(
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long actor,
            @PositiveOrZero(message = "Page must be a positive number") @RequestParam(required = false) Integer page,
            @Positive(message = "Size must be a positive number") @RequestParam(required = false) Integer size) {

        // If pagination parameters are provided, apply pagination
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> moviePage = movieService.getFilteredPageableMovies(genre, year, actor, pageable);

            // Return a message if no movies are found in the current page
            if (moviePage.getContent().isEmpty()) {
                return ResponseEntity.ok().body("No movies found.");
            }

            return ResponseEntity.ok(moviePage);
        }

        // Retrieve all filtered movies without pagination
        List<Movie> filteredMovies = movieService.getFilteredMovies(genre, year, actor);

        if (filteredMovies.isEmpty()) {
            return ResponseEntity.ok().body("No movies found.");
        }

        return ResponseEntity.ok().body(filteredMovies);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<?> searchMoviesByTitle(@RequestParam String title) {
        List<Movie> matchingMovies = movieService.findByTitleContains(title);
        if (matchingMovies.isEmpty()) {
            return ResponseEntity.ok().body("No movies found");
        }
        return ResponseEntity.ok().body(matchingMovies);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieService.getMovieById(id);
        if (!movieOptional.isPresent()) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
        return ResponseEntity.ok(movieOptional.get());
    }

    @GetMapping("/movies/{id}/actors")
    public ResponseEntity<?> getActorsByMovieId(@PathVariable Long id) {
        Optional<Movie> movieOptional = movieService.getMovieById(id);
        if (!movieOptional.isPresent()) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }

        Movie movie = movieOptional.get();

        // Map movies to a simplified structure
        List<Map<String, Object>> associatedMovies = movie.getActors().stream()
                .map(actor -> {
                    Map<String, Object> movieDetails = new HashMap<>();
                    movieDetails.put("actor_id", actor.getId());
                    movieDetails.put("actor_name", actor.getName());
                    movieDetails.put("actor_birthdate", actor.getBirthdate());
                    return movieDetails;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("movie_id", movie.getId());
        response.put("movie_title", movie.getTitle());
        response.put("associated_actors", associatedMovies);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getMoviesByGenre(@RequestParam("genre") Long id) {
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

    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        // Validate and set genre if provided
        if (movie.getGenres() != null) {
            Set<Genre> validGenres = movie.getGenres().stream()
                    .map(genre -> genreService.getGenreById(genre.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Genre not found")))
                    .collect(Collectors.toSet());
            movie.setGenres(validGenres);
        }

        // Validate and set actors if provided
        if (movie.getActors() != null && !movie.getActors().isEmpty()) {
            Set<Actor> validActors = movie.getActors().stream()
                    .map(actor -> actorService.getActorById(actor.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Actor not found")))
                    .collect(Collectors.toSet());
            movie.setActors(validActors);
        }

        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.status(201).body(savedMovie);
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id) {
        if (!movieService.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
        String movieTitle = movieService.getMovieById(id).get().getTitle();
        movieService.deleteMovie(id);
        return ResponseEntity.status(204).body("Movie '" + movieTitle + "' has been successfully deleted");
    }

    @PatchMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        if (!movieService.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }

        // Fetch the existing movie
        Movie existingMovie = movieService.getMovieById(id).get();

        // Update fields if they are provided
        if (movie.getTitle() != null) {
            existingMovie.setTitle(movie.getTitle());
        }
        if (movie.getReleaseYear() != null) {
            existingMovie.setReleaseYear(movie.getReleaseYear());
        }
        if (movie.getDuration() != 0) {
            existingMovie.setDuration(movie.getDuration());
        }
        if (movie.getGenres() != null) {
            Set<Genre> genres = movie.getGenres().stream()
                    .map(genre -> genreService.getGenreById(genre.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Genre not found with ID: " + genre.getId())))
                    .collect(Collectors.toSet());
            existingMovie.setGenres(genres);
        }
        if (movie.getActors() != null && !movie.getActors().isEmpty()) {
            Set<Actor> updatedActors = movie.getActors().stream()
                    .map(actor -> actorService.getActorById(actor.getId())
                            .orElseThrow(
                                    () -> new ResourceNotFoundException("Actor not found with ID: " + actor.getId())))
                    .collect(Collectors.toSet());
            movieService.updateMovieActors(id, updatedActors);
        }

        Movie updatedMovie = movieService.saveMovie(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }
}
