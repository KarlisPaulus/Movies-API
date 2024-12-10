package com.example.moviesapi;

// import com.example.moviesapi.api.entities.Movie;
import com.example.moviesapi.api.repository.MovieRepository;
import com.example.moviesapi.api.repository.GenreRepository;
import com.example.moviesapi.api.repository.ActorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.moviesapi.api.entities.Genre;
import com.example.moviesapi.api.entities.Actor;
import com.example.moviesapi.api.entities.Movie;
import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

@SuppressWarnings("unused")
@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        return args -> {
            // Clear existing data
            movieRepository.deleteAll();
            actorRepository.deleteAll();
            genreRepository.deleteAll();
            
            Actor danielCraig = actorRepository.saveAndFlush(new Actor("Daniel Craig", LocalDate.of(1968, 3, 2)));
            Actor jenniferLawrence = actorRepository.saveAndFlush(new Actor("Jennifer Lawrence", LocalDate.of(1990, 8, 15)));
            Actor keanuReeves = actorRepository.saveAndFlush(new Actor("Keanu Reeves", LocalDate.of(1964, 9, 2)));
            Actor brieLarson = actorRepository.saveAndFlush(new Actor("Brie Larson", LocalDate.of(1989, 10, 1)));
            Actor denzelWashington = actorRepository.saveAndFlush(new Actor("Denzel Washington", LocalDate.of(1954, 12, 28)));
            Actor emilyBlunt = actorRepository.saveAndFlush(new Actor("Emily Blunt", LocalDate.of(1983, 2, 23)));
            Actor hughJackman = actorRepository.saveAndFlush(new Actor("Hugh Jackman", LocalDate.of(1968, 10, 12)));
            Actor bradPitt = actorRepository.saveAndFlush(new Actor("Brad Pitt", LocalDate.of(1963, 12, 18)));
            Actor willSmith = actorRepository.saveAndFlush(new Actor("Will Smith", LocalDate.of(1968, 9, 25)));
            Actor margotRobbie = actorRepository.saveAndFlush(new Actor("Margot Robbie", LocalDate.of(1990, 7, 2)));
            Actor benedictCumberbatch = actorRepository.saveAndFlush(new Actor("Benedict Cumberbatch", LocalDate.of(1976, 7, 19)));
            Actor cateBlanchett = actorRepository.saveAndFlush(new Actor("Cate Blanchett", LocalDate.of(1969, 5, 14)));
            Actor mattDamon = actorRepository.saveAndFlush(new Actor("Matt Damon", LocalDate.of(1970, 10, 8)));
            Actor amyAdams = actorRepository.saveAndFlush(new Actor("Amy Adams", LocalDate.of(1974, 8, 20)));
            Actor tomCruise = actorRepository.saveAndFlush(new Actor("Tom Cruise", LocalDate.of(1962, 7, 3)));
            Actor charlizeTheron = actorRepository.saveAndFlush(new Actor("Charlize Theron", LocalDate.of(1975, 8, 7)));
            Actor chadwickBoseman = actorRepository.saveAndFlush(new Actor("Chadwick Boseman", LocalDate.of(1976, 11, 29)));

            Genre action = genreRepository.saveAndFlush(new Genre("Action"));
            Genre drama = genreRepository.saveAndFlush(new Genre("Drama"));
            Genre sciFi = genreRepository.saveAndFlush(new Genre("Sci-Fi"));
            Genre thriller = genreRepository.saveAndFlush(new Genre("Thriller"));
            Genre romance = genreRepository.saveAndFlush(new Genre("Romance"));
            Genre fantasy = genreRepository.saveAndFlush(new Genre("Fantasy"));

            movieRepository.saveAndFlush(new Movie("Skyfall", Year.of(2012), 8520, Set.of(danielCraig), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("Silver Linings Playbook", Year.of(2012), 7800, Set.of(jenniferLawrence), Set.of(drama, romance)));
            movieRepository.saveAndFlush(new Movie("John Wick", Year.of(2014), 9020, Set.of(keanuReeves), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("Room", Year.of(2015), 8120, Set.of(brieLarson), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("Fences", Year.of(2016), 8200, Set.of(denzelWashington), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("A Quiet Place", Year.of(2018), 8100, Set.of(emilyBlunt), Set.of(drama, thriller)));
            movieRepository.saveAndFlush(new Movie("The Greatest Showman", Year.of(2017), 8230, Set.of(hughJackman), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("I, Tonya", Year.of(2017), 7500, Set.of(margotRobbie), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("Doctor Strange", Year.of(2016), 8340, Set.of(benedictCumberbatch), Set.of(action, sciFi)));
            movieRepository.saveAndFlush(new Movie("Blue Jasmine", Year.of(2013), 8020, Set.of(cateBlanchett), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("The Martian", Year.of(2015), 8740, Set.of(mattDamon), Set.of(drama, sciFi)));
            movieRepository.saveAndFlush(new Movie("Arrival", Year.of(2016), 7280, Set.of(amyAdams), Set.of(drama, sciFi)));
            movieRepository.saveAndFlush(new Movie("Mission: Impossible - Fallout", Year.of(2018), 8540, Set.of(tomCruise), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("Atomic Blonde", Year.of(2017), 8120, Set.of(charlizeTheron), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("Black Panther", Year.of(2018), 9120, Set.of(chadwickBoseman), Set.of(action, drama, sciFi)));
            movieRepository.saveAndFlush(new Movie("Passengers", Year.of(2016), 6240, Set.of(jenniferLawrence), Set.of(romance, sciFi)));
            movieRepository.saveAndFlush(new Movie("Constantine", Year.of(2005), 7240, Set.of(keanuReeves), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("Jack Reacher", Year.of(2012), 8120, Set.of(tomCruise), Set.of(action, thriller)));
            movieRepository.saveAndFlush(new Movie("The Pursuit of Happyness", Year.of(2006), 7600, Set.of(willSmith), Set.of(drama)));
            movieRepository.saveAndFlush(new Movie("The Curious Case of Benjamin Button", Year.of(2008), 8700, Set.of(bradPitt, cateBlanchett), Set.of(drama, fantasy, romance)));
        };
    }
}