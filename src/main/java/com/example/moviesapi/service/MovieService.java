package com.example.moviesapi.service;

import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.api.entities.Actor;
import com.example.moviesapi.api.entities.Genre;
import com.example.moviesapi.api.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.time.Year;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    public List<Movie> findMoviesByGenre(Long genreId) {
        return movieRepository.findByGenresId(genreId);
    }

    public List<Movie> findMoviesByYear(Year releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }

    public List<Movie> findMoviesByActor(Long actorId) {
        return movieRepository.findByActorsId(actorId);
    }

    public boolean existsById(Long movieId) {
        return movieRepository.existsById(movieId);
    }

    public List<Movie> getFilteredMovies(Long genreId, Integer year, Long actorId) {
        Year releaseYear = (year != null) ? Year.of(year) : null;

        if (genreId != null && releaseYear != null && actorId != null) {
            return movieRepository.findByGenresIdAndReleaseYearAndActorsId(genreId, releaseYear, actorId);
        } else if (genreId != null && releaseYear != null) {
            return movieRepository.findByGenresIdAndReleaseYear(genreId, releaseYear);
        } else if (genreId != null && actorId != null) {
            return movieRepository.findByGenresIdAndActorsId(genreId, actorId);
        } else if (releaseYear != null && actorId != null) {
            return movieRepository.findByReleaseYearAndActorsId(releaseYear, actorId);
        } else if (genreId != null) {
            return movieRepository.findByGenresId(genreId);
        } else if (releaseYear != null) {
            return movieRepository.findByReleaseYear(releaseYear);
        } else if (actorId != null) {
            return movieRepository.findByActorsId(actorId);
        } else {
            return movieRepository.findAll();
        }
    }

    public Page<Movie> getFilteredPageableMovies(Long genreId, Integer year, Long actorId, Pageable pageable) {
        Year releaseYear = (year != null) ? Year.of(year) : null;

        if (genreId != null && releaseYear != null && actorId != null) {
            return movieRepository.findByGenresIdAndReleaseYearAndActorsId(genreId, releaseYear, actorId, pageable);
        } else if (genreId != null && releaseYear != null) {
            return movieRepository.findByGenresIdAndReleaseYear(genreId, releaseYear, pageable);
        } else if (genreId != null && actorId != null) {
            return movieRepository.findByGenresIdAndActorsId(genreId, actorId, pageable);
        } else if (releaseYear != null && actorId != null) {
            return movieRepository.findByReleaseYearAndActorsId(releaseYear, actorId, pageable);
        } else if (genreId != null) {
            return movieRepository.findByGenresId(genreId, pageable);
        } else if (releaseYear != null) {
            return movieRepository.findByReleaseYear(releaseYear, pageable);
        } else if (actorId != null) {
            return movieRepository.findByActorsId(actorId, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }

    public Movie updateMovie(Long movieId, String title, Year releaseYear, int duration, Set<Actor> actors, Set<Genre> genres) {
        Optional<Movie> optionalMovie = getMovieById(movieId);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            movie.setTitle(title);
            movie.setReleaseYear(releaseYear);
            movie.setDuration(duration);
            if (actors != null) {
                movie.setActors(actors);
            }
            if (genres != null) {
                movie.setGenres(genres);
            }
            return movieRepository.save(movie);
        } else {
            throw new RuntimeException("Movie not found with ID: " + movieId);
        }
    }

    public void updateMovieActors(Long movieId, Set<Actor> updatedActors) {
        Movie movie = getMovieById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        // Remove movie from actors that are no longer associated
        movie.getActors().stream()
                .filter(actor -> !updatedActors.contains(actor))
                .forEach(actor -> actor.getMovies().remove(movie));

        // Add movie to new actors
        updatedActors.stream()
                .filter(actor -> !movie.getActors().contains(actor))
                .forEach(actor -> actor.getMovies().add(movie));

        movie.setActors(updatedActors);
        movieRepository.save(movie);
    }

    public void removeActorFromMovie(Long movieId, Actor actor) {
        Movie movie = getMovieById(movieId)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        if (movie.getActors().contains(actor)) {
            movie.getActors().remove(actor);
            actor.getMovies().remove(movie);
            movieRepository.save(movie);
        }
    }

    public List<Movie> findByTitleContains(String titleFragment) {
        return movieRepository.findByTitleContains(titleFragment);
    }

    public void deleteMovie(Long movieId) {
        movieRepository.deleteById(movieId);
    }
}
