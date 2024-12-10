package com.example.moviesapi.api.entities;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

@Entity
public class Actor {

    @JsonIgnore
    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Actor name cannot be null.")
    @Size(min = 2, max = 300, message = "Actor name must be between 2 and 300 characters.")
    @Pattern(regexp = "[A-Za-z -.]+", message = "Actor name must contain only alphabetic characters, spaces, or hyphens.")
    private String name;

    @NotNull(message = "Actor birthdate cannot be null")
    @PastOrPresent(message = "Actor birthdate cannot be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    // Constructors
    public Actor() {
    }

    public Actor(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public Actor(Long id, String name, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

     // Getters
     public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id) &&
                Objects.equals(name, actor.name) &&
                Objects.equals(birthdate, actor.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthdate);
    }

    // Override toString for readable representation

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
