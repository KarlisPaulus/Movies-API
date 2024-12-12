package com.example.moviesapi.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import com.example.moviesapi.api.entities.Actor;
import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.service.MovieService;
import com.example.moviesapi.service.ActorService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

@Validated
@RestController
@RequestMapping("/api")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private PagedResourcesAssembler<Actor> pagedResourcesAssembler;

    @GetMapping("/actors")
    public ResponseEntity<?> getAllActors(
            @PositiveOrZero(message = "Page number must be positive or zero.") @RequestParam(required = false) Integer page,
            @Positive(message = "Size must be a positive number") @RequestParam(required = false) Integer size) {

        // Apply pagination if page and size are provided
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Actor> actorsPage = actorService.getAllPageableActors(pageable);
            PagedModel<EntityModel<Actor>> pagedModel = pagedResourcesAssembler.toModel(actorsPage);
            return ResponseEntity.ok(pagedModel);
        }

        List<Actor> actors = actorService.getAllActors();

        if (actors.isEmpty()) {
            return ResponseEntity.ok("No actors found.");
        }

        return ResponseEntity.ok(actors);
    }

    @GetMapping("/actors/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable("id") Long id) {
        Optional<Actor> actor = actorService.getActorById(id);
        if (!actor.isPresent()) {
            throw new ResourceNotFoundException("Actor not found with ID: " + id);
        }
        return ResponseEntity.ok(actor.get());
    }

    @GetMapping("/actors/{id}/movies")
    public ResponseEntity<?> getActorAndAssociatedMovies(@PathVariable Long id) {
        Optional<Actor> actorOptional = actorService.getActorById(id);
        if (!actorOptional.isPresent()) {
            throw new ResourceNotFoundException("Actor not found with ID: " + id);
        }

        Actor actor = actorOptional.get();

        // Map movies to a simplified structure
        List<Map<String, Object>> associatedMovies = actor.getMovies().stream()
                .map(movie -> {
                    Map<String, Object> movieDetails = new HashMap<>();
                    movieDetails.put("movie_id", movie.getId());
                    movieDetails.put("movie_title", movie.getTitle());
                    return movieDetails;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("actor_id", actor.getId());
        response.put("actor_name", actor.getName());
        response.put("associated_movies", associatedMovies);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/actors/search")
    public ResponseEntity<?> searchActorsByName(@RequestParam("name") String name) {
        List<Actor> matchingActors = actorService.getActorByName(name);
        if (matchingActors.isEmpty()) {
            return ResponseEntity.ok().body("No actors found matching the name: " + name);
        }
        return ResponseEntity.ok(matchingActors);
    }

    @GetMapping("/actors/sort")
    public ResponseEntity<List<Actor>> getSortedActors(
        @PositiveOrZero(message = "Page number must be positive or zero.") @RequestParam(required = false) Integer page,
        @Positive(message = "Size must be a positive number") @RequestParam(required = false) Integer size) {

        // Apply pagination if page and size are provided
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
            Page<Actor> actorsPage = actorService.getAllPageableActors(pageable);
            
            return ResponseEntity.ok(actorsPage.getContent());
        }
        List<Actor> sortedActors = actorService.getAlphabeticallySortedActors();
        
        return ResponseEntity.ok(sortedActors);
    }

    @PostMapping("/actors")
    public ResponseEntity<Actor> createActor(@Valid @RequestBody Actor actor) {
        Actor savedActor = actorService.saveActor(actor);
        return ResponseEntity.status(201).body(savedActor);
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<?> deleteActor(
            @PathVariable Long id,
            @RequestParam(value = "cascade", required = false, defaultValue = "false") boolean cascade) {

        if (!actorService.existsById(id)) {
            throw new ResourceNotFoundException("Actor not found with ID: " + id);
        }

        Actor actor = actorService.getActorById(id).get();
        int associatedMoviesCount = actor.getMovies().size();
        String actorName = actor.getName();

        if (associatedMoviesCount > 0) {
            if (cascade) {
                // Remove from all associated movies
                for (Movie movie : actor.getMovies()) {
                    movieService.removeActorFromMovie(movie.getId(), actor);
                }
                actorService.deleteActor(id);
                // String successMessage = "Actor '" + actorName + "' and their " + associatedMoviesCount
                //         + " associated movies have been deleted.";
                // String successMessage = "Actor '" + actorName + "' has been deleted and removed from all associated movies.";
                return ResponseEntity.status(204).build();
            } else {
                String errorMessage = "Cannot delete actor '" + actorName
                        + "' as they are associated with " + associatedMoviesCount + " movie(s).";
                return ResponseEntity.status(400).body(errorMessage);
            }
        }

        actorService.deleteActor(id);
        return ResponseEntity.status(204).body("Actor '" + actorName + "' deleted successfully.");
    }

    @PatchMapping("/actors/{id}")
    public ResponseEntity<Actor> updateActorName(
            @PathVariable Long id,
            @RequestBody Actor actor) {

        if (!actorService.existsById(id)) {
            throw new ResourceNotFoundException("Actor not found with ID: " + id);
        }

        Actor updatedActor = actorService.updateActor(id, actor.getName());
        return ResponseEntity.ok(updatedActor);
    }

    @PatchMapping("/actors/{id}/movies")
    public ResponseEntity<?> updateActorMovies(
            @PathVariable Long id,
            @RequestBody Set<Long> movieIds) {

        if (!actorService.existsById(id)) {
            throw new ResourceNotFoundException("Actor not found with ID: " + id);
        }

        // Fetch and validate all movies by IDs
        Set<Movie> updatedMovies = movieIds.stream()
                .map(movieId -> movieService.getMovieById(movieId)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId)))
                .collect(Collectors.toSet());

        actorService.updateActorMovies(id, updatedMovies);
        ResponseEntity<?> associatedMovies = getActorAndAssociatedMovies(id);
        return ResponseEntity.ok(associatedMovies);
    }
}
