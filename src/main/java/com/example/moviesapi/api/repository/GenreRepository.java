package com.example.moviesapi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.moviesapi.api.entities.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}