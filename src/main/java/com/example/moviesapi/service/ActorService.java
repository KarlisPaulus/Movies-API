package com.example.moviesapi.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.moviesapi.api.entities.Actor;
import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.api.repository.ActorRepository;


@Service
public class ActorService {

    @Autowired
    private ActorRepository actorRepository;

    public Actor saveActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    public Page<Actor> getAllPageableActors(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Optional<Actor> getActorById(Long actorId) {
        return actorRepository.findById(actorId);
    }

    public List<Actor> getActorByName(String nameFragment) {
        return actorRepository.findByNameContains(nameFragment);
    }

    public Set<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = getActorById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + actorId));
        return actor.getMovies();
    }

    public List<Actor> getAlphabeticallySortedActors() {
        return actorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
    
     // Actor name update
     public Actor updateActor(Long actorId, String name) {
        Optional<Actor> optionalActor = getActorById(actorId);
        optionalActor.ifPresent(actor -> {actor.setName(name);
            actorRepository.save(actor);
        });
        return optionalActor
                .orElseThrow(() -> new RuntimeException("Actor not found with ID: " + actorId));
    }

    public void updateActorMovies(Long actorId, Set<Movie> updatedMovies) {
        Actor actor = getActorById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + actorId));

        // Removes actor from movies that are no longer relevant
        actor.getMovies().stream()
                .filter(movie -> !updatedMovies.contains(movie))
                .forEach(movie -> movie.getActors().remove(actor));

        // Add actor to new movies
        updatedMovies.stream()
                .filter(movie -> !actor.getMovies().contains(movie))
                .forEach(movie -> movie.getActors().add(actor));

        actor.setMovies(updatedMovies);
        actorRepository.save(actor);
    }

    public boolean existsById(Long actorId) {
        return actorRepository.existsById(actorId);
    }
    
    public void deleteActor(Long actorId) {
        actorRepository.deleteById(actorId);
    }
}