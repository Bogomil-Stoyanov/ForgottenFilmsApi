package eu.bbsapps.forgottenfilmsapi.data.controllers


import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem
import eu.bbsapps.forgottenfilmsapi.data.responses.FilmFeedResponse
import eu.bbsapps.forgottenfilmsapi.data.responses.MovieResponse
import eu.bbsapps.forgottenfilmsapi.database
import java.util.stream.Collectors

object FilmsController {

    /**
     * Checks if the film with id filmId is already liked by a user with userId else false
     */
    suspend fun isMovieLikedBy(filmId: String, userId: String): Boolean {
        return database.isMovieLikedBy(filmId, userId)
    }

    /**
     * Removes a like from a film with filmId by a user with userId
     * @return True if it the removal was successful else false
     */
    suspend fun removeLikeFromMovie(filmId: String, userId: String): Boolean {
        return database.removeLikeFromMovie(filmId, userId)
    }

    /**
     * Checks if the film with id filmId is already disliked by a user with userId else false
     */
    suspend fun isMovieDislikedBy(filmId: String, userId: String): Boolean {
        return database.isMovieDislikedBy(filmId, userId)
    }

    /**
     * Removes a dislike from a film with filmId by a user with userId
     * @return True if it the removal was successful else false
     */
    suspend fun removeDislikeFromMovie(filmId: String, userId: String): Boolean {
        return database.removeDislikeFromMovie(filmId, userId)
    }

    /**
     * Adds a like from a film with filmId by a user with userId
     * @return True if it the addition was successful else false
     */
    suspend fun addLikeToMovie(filmId: String, userId: String): Boolean {
        return database.addLikeToMovie(filmId, userId)
    }

    /**
     * Adds a dislike from a film with filmId by a user with userId
     * @return True if it the addition was successful else false
     */
    suspend fun addDislikeToMovie(filmId: String, userId: String): Boolean {
        return database.addDislikeToMovie(filmId, userId)
    }

    /**
     * Gets the total like count of a film
     * @return The like count of a film with filmId
     */
    suspend fun getLikeCountForFilm(filmId: String): Int {
        return database.getLikeCountForMovie(filmId)
    }

    /**
     * Gets the total dislike count of a film
     * @return The dislike count of a film with filmId
     */
    suspend fun getDislikeCountForFilm(filmId: String): Int {
        return database.getDislikeCountForMovie(filmId)
    }

    /**
     * Generated a MovieResponse response for a user
     * @return A MovieResponse and if the user has liked/dislike the film
     */
    suspend fun getFilmForUser(filmId: String, userId: String): MovieResponse {
        val film = database.getMovieWithId(filmId)

        var isLiked = 0
        if (database.isMovieLikedBy(filmId, userId)) {
            isLiked = 1
        } else if (database.isMovieDislikedBy(filmId, userId)) {
            isLiked = -1
        }

        return MovieResponse(
            name = film.name,
            imageUrls = film.imageUrls,
            description = film.description,
            categories = film.genres,
            likes = film.likedBy.size,
            dislikes = film.dislikedBy.size,
            isLiked = isLiked,
            url = film.url,
            id = film.id
        )
    }

    /**
     * Generates user feed on the following algorithm:
     * 1. Adds up to 5 films on every genre that the user has as favourite
     * 2. Adds up to 5 random films that the user may like
     * 3. Adds the Top 10 Most Liked Movies
     */
    suspend fun getUserFeed(userId: String): List<FilmFeedResponse> {
        val interests = database.getUserGenres(userId)

        val feed = ArrayList<FilmFeedResponse>()

        interests.forEach { interest ->
            //gets up to 5 random films the the provided genre
            val filmsWithCategory = database.getRandomMoviesWithGenre(interest).shuffled().map {
                FilmFeedItem(it.name, it.imageUrls[0], it.id)
            }
            feed.add(FilmFeedResponse(interest, filmsWithCategory))
        }

        var films = database.getAllMovies()

        films = films.shuffled()

        val randomFeedItems = database.getRandomMovies().map {
            FilmFeedItem(it.name, it.imageUrls[0], it.id)
        }
        feed.add(FilmFeedResponse("Може да ти хареса", randomFeedItems))

        films = films.sortedByDescending { it.likedBy.size }
        films = films.subList(0, 10)

        val mostLikedFeedItems = ArrayList<FilmFeedItem>()

        films.forEach { film ->
            mostLikedFeedItems.add(FilmFeedItem(film.name, film.imageUrls[0], film.id))
        }

        feed.add(FilmFeedResponse("Най-харесвани филми", mostLikedFeedItems))

        return feed
    }

    /**
     * Searches all film titles on the given query
     */
    suspend fun searchMovieByTitle(query: String): List<FilmFeedItem> {
        return database.searchForMovies(query).map {
            FilmFeedItem(it.name, it.imageUrls[0], it.id)
        }
    }

    /**
     * @return True if the film is in user's list else false
     */
    suspend fun isFilmInUserList(filmId: String, email: String): Boolean {
        return database.isMovieAddedToList(filmId, email)
    }

    /**
     * Gets a list of all films
     * @return List of all films grouped by their main genre
     */
    suspend fun getAllMovies(): List<FilmFeedResponse> {
        val allMovies = database.getAllMovies()
        val groupedByGenres =
            allMovies.stream().collect(Collectors.groupingBy { film -> film.genres.first() })
        val filmsSortedByCategories = mutableListOf<FilmFeedResponse>()
        for (entry in groupedByGenres.entries) {
            val list = entry.value.toList().map {
                FilmFeedItem(it.name, it.imageUrls[0], it.id)
            }
            filmsSortedByCategories.add(FilmFeedResponse(entry.key, list))
        }
        filmsSortedByCategories.sortBy(FilmFeedResponse::title)
        return filmsSortedByCategories
    }

    /**
     * Aggregates a list of all genres that films have in database and sorts them alphabetically
     * @return A list of all genres sorted alphabetically
     */
    suspend fun getAllGenres(): List<String> {
        return database.getAllMovies().stream().collect(
            Collectors.groupingBy { film -> film.genres.first() }
        ).keys.sorted()
    }
}
