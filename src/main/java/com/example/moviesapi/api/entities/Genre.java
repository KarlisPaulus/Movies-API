package com.example.moviesapi.api.entities;

import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Genre {
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Genre name can't be null")
    @Size(min = 1, max = 30, message = "Genre name must be between 1 to 30 characters")
    private String name;

    // Constructors
    public Genre () {
    }
 
    public Genre(String name) {
        this.name = name;
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Set<Movie> getMovies() {
        return movies;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        return true;
        if (obj == null || getClass() != obj.getClass())
        return false;

        Genre genre = (Genre) obj;
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    // For readable representation

    @Override
    public String toString() {
        return "Genre{" + "id=" + id + ", name" + name + '\'' + '}'; 
    }

    
}

