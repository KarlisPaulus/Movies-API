package com.example.moviesapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.moviesapi.api.entities.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByNameContains(String name);
}
