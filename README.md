# Movies API

## Project Overview

The Movies API is a RESTful service developed in Java using Spring Boot. It provides functionality for managing and retrieving details about movies, genres and actors. The API supports CRUD operations (Create, Read, Update and Delete) for all entities and includes a global exception handler for effective error management.

## Setup and installation instructions

### Installation Steps
1. **Clone the Repository:**
    ```bash
    git clone https://gitea.kood.tech/karlispaulus/kmdb.git
    ```
2. **Navigate to the Project Directory:**
    ```bash
    cd kmdb
    ```
3. **Build the Project Using Maven:**
    ```bash
    mvn clean install
    ```
4. **Run the Application:**
    ```bash
    mvn spring-boot:run
    ```
5. **Access the API:**
    The API will be available at `http://localhost:8080/api/`

## Usage Guide

### Sample data

LoadDatabase.java will automatically insert sample data into the database. The modifed data will be reset when the application is restarted.

### Postman

Import the `MovieDatabaseAPI.postman_collection.json` collection into Postman to test the endpoints.

### API Endpoints

#### SwaggerUI Api docs (Bonus functionality)

Available at `http://localhost:8080/swagger-ui/index.html`

#### Actors
- **GET All Actors**
    - **Endpoint:** `GET /api/actors`
    - **Parameters:**
        - `page`: Page number (optional)
        - `size`: Page size (optional)
    - **Description:** Retrieves a list of all actors.
- **GET Actor by ID**
    - **Endpoint:** `GET /api/actors/{id}`
    - **Description:** Retrieves details of a specific actor by their ID.
- **GET Actor by Name**
    - **Endpoint:** `GET /api/actors/search?name={name}`
    - **Description:** Retrieves actors by their name.
- **GET Actor by Associated Movies**
    - **Endpoint:** `GET /api/actors/{id}/movies`
    - **Description:** Retrieves a list of movies associated with an actor by their ID.
- **Create a New Actor**
    - **Endpoint:** `POST /api/actors`
    - **Description:** Adds a new actor to the database.
- **Update an Existing Actor**
    - **Endpoint:** `PATCH /api/actors/{id}`
    - **Description:** Updates the details of an existing actor.
- **Update an Existing Actors Movies**
    - **Endpoint:** `PATCH /api/actors/{id}/movies`
    - **Description:** Updates the movies of an existing actor.
- **DELETE an Actor**
    - **Endpoint:** `DELETE /api/actors/{id}`
    - **Parameters:**
        - `cascade`: Must be set to true, if the actor has associated movies, the actor will be deleted along with the movies.
    - **Description:** Removes an actor from the database.

#### Movies
- **GET All Movies**
    - **Endpoint:** `GET /api/movies`
    - **Parameters:**
        - `page`: Page number (optional)
        - `size`: Page size (optional)
        - `year`: Year of the movie (optional)
        - `genre`: Genre of the movie (optional)
        - `actor`: Actor in the movie (optional)
    - **Description:** Retrieves a list of all movies.
- **GET Movie by ID**
    - **Endpoint:** `GET /api/movies/{id}`
    - **Description:** Retrieves details of a specific movie by its ID.
- **GET Movie by Title**
    - **Endpoint:** `GET /api/movies/search?title={title}`
    - **Description:** Retrieves movies by their title.
- **GET Movie Actors**
    - **Endpoint:** `GET /api/movies/{id}/actors`
    - **Description:** Retrieves actors associated with a movie by its ID.
- **Create a New Movie**
    - **Endpoint:** `POST /api/movies`
    - **Description:** Adds a new movie to the database.
- **Update an Existing Movie**
    - **Endpoint:** `PUT /api/movies/{id}`
    - **Description:** Updates the details of an existing movie.
- **DELETE a Movie**
    - **Endpoint:** `DELETE /api/movies/{id}`
    - **Description:** Removes a movie from the database.

#### Genres
- **GET All Genres**
    - **Endpoint:** `GET /api/genres`
    - **Parameters:**
        - `page`: Page number (optional)
        - `size`: Page size (optional)
    - **Description:** Retrieves a list of all genres.
- **GET Genre by ID**
    - **Endpoint:** `GET /api/genres/{id}`
    - **Description:** Retrieves details of a specific genre by its ID.
- **GET Genre Movies**
    - **Endpoint:** `GET /api/genres/{id}/movies`
    - **Description:** Retrieves movies associated with a genre by its ID.
- **Create a New Genre**
    - **Endpoint:** `POST /api/genres`
    - **Description:** Adds a new genre to the database.
- **Update an Existing Genre**
    - **Endpoint:** `PUT /api/genres/{id}`
    - **Description:** Updates the details of an existing genre.
- **DELETE a Genre**
    - **Endpoint:** `DELETE /api/genres/{id}`
    - **Description:** Removes a genre from the database.

## Bonus functionality 🎁

### Swagger-UI

Automatically generated API documentation with Swagger-UI.
It makes easier to understand, explore and test the API without needing external tools.
