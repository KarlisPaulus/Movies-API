package com.example.moviesapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.moviesapi.api.entities.Genre;
import com.example.moviesapi.api.repository.GenreRepository;

import org.springframework.stereotype.Service;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Page<Genre> getAllPageableGenres(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    public Optional<Genre> getGenreById(Long genreId) {
        return genreRepository.findById(genreId);
    }
    
    public Genre updateGenre(Long genreId, String name) {
        Optional<Genre> optionalGenre = getGenreById(genreId);
        optionalGenre.ifPresent(genre -> {genre.setName(name);
            genreRepository.save(genre);
        });
        return optionalGenre
                .orElseThrow(() -> new RuntimeException("Genre not found with ID: " + genreId));
    }

    public boolean existsById(Long genreId) {
        return genreRepository.existsById(genreId);
    }

    public void deleteGenre(Long genreId) {
        genreRepository.deleteById(genreId);
    }
}