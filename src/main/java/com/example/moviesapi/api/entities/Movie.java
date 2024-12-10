package com.example.moviesapi.api.entities;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.time.Year;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@Entity
public class Movie {
    
    @ManyToMany
    @JoinTable(name = "movie_actor", joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"))
    
    private Set<Actor> actors;

    @ManyToMany
    @JoinTable(name = "movie_genre", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))

    private Set<Genre> genres;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title can't be null")
    @Size(min = 1, max = 255, message = "Title length must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Release year can't be null")
    @PastOrPresent(message = "Release year can't be in the future")
    private Year releaseYear;

    @NotNull(message = "Duration can't be null")
    @Min(value = 2400, message = "A movie must be at least 40 minutes long")
    @Max(value = 3085200, message = "A movie can't be longer than 857 hours")
    private int duration; // Duration in seconds

    // Constructors
    public Movie() {
    }

    public Movie(String title, Year releaseYear, int duration, Set<Actor> actors, Set<Genre> genres) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.actors = actors;
        this.genres = genres;
    }

    public Movie(Long id, String title, Year releaseYear, int duration, Set<Actor> actors, Set<Genre> genres) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.actors = actors;
        this.genres = genres;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Year getReleaseYear() {
        return releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseYear(Year releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return id.equals(movie.id) && Objects.equals(title, movie.title) && Objects.equals(releaseYear, movie.releaseYear) && duration == movie.duration && Objects.equals(actors, movie.actors) && Objects.equals(genres, movie.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, releaseYear, duration, actors, genres);
    }

    // For readable representation

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title='" + title + '\'' + ", releaseYear=" + releaseYear + ", duration=" + duration + ", actors=" + actors + ", genres=" + genres + '}';
    }
}
