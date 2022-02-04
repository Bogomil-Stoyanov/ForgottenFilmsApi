package eu.bbsapps.forgottenfilmsapi.data.controllers


import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem
import eu.bbsapps.forgottenfilmsapi.data.responses.FilmFeedResponse
import eu.bbsapps.forgottenfilmsapi.data.responses.FilmResponse
import eu.bbsapps.forgottenfilmsapi.database
import java.util.stream.Collectors

object FilmsController {

    /**
     * Checks if the film with id filmId is already liked by a user with userId else false
     */
    suspend fun isFilmLikedBy(filmId: String, userId: String): Boolean {
        return database.isFilmLikedBy(filmId, userId)
    }

    /**
     * Removes a like from a film with filmId by a user with userId
     * @return True if it the removal was successful else false
     */
    suspend fun removeLikeFromFilm(filmId: String, userId: String): Boolean {
        return database.removeLikeFromFilm(filmId, userId)
    }

    /**
     * Checks if the film with id filmId is already disliked by a user with userId else false
     */
    suspend fun isFilmDislikedBy(filmId: String, userId: String): Boolean {
        return database.isFilmDislikedBy(filmId, userId)
    }

    /**
     * Removes a dislike from a film with filmId by a user with userId
     * @return True if it the removal was successful else false
     */
    suspend fun removeDislikeFromFilm(filmId: String, userId: String): Boolean {
        return database.removeDislikeFromFilm(filmId, userId)
    }

    /**
     * Adds a like from a film with filmId by a user with userId
     * @return True if it the addition was successful else false
     */
    suspend fun addLikeToFilm(filmId: String, userId: String): Boolean {
        return database.addLikeToFilm(filmId, userId)
    }

    /**
     * Adds a dislike from a film with filmId by a user with userId
     * @return True if it the addition was successful else false
     */
    suspend fun addDislikeToFilm(filmId: String, userId: String): Boolean {
        return database.addDislikeToFilm(filmId, userId)
    }

    /**
     * Gets the total like count of a film
     * @return The like count of a film with filmId
     */
    suspend fun getLikeCountForFilm(filmId: String): Int {
        return database.getLikeCountForFilm(filmId)
    }

    /**
     * Gets the total dislike count of a film
     * @return The dislike count of a film with filmId
     */
    suspend fun getDislikeCountForFilm(filmId: String): Int {
        return database.getDislikeCountForFilm(filmId)
    }

    /**
     * Generated a FilmResponse response for a user
     * @return A FilmResponse and if the user has liked/dislike the film
     */
    suspend fun getFilmForUser(filmId: String, userId: String): FilmResponse {
        val film = database.getFilmWithId(filmId)

        var isLiked = 0
        if (database.isFilmLikedBy(filmId, userId)) {
            isLiked = 1
        } else if (database.isFilmDislikedBy(filmId, userId)) {
            isLiked = -1
        }

        return FilmResponse(
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
     * 3. Adds the Top 10 Most Liked Films
     */
    suspend fun getUserFeed(email: String): List<FilmFeedResponse> {
        val interests = database.getUserGenres(email).sorted()


        val feed = ArrayList<FilmFeedResponse>()

        interests.forEach { interest ->
            //gets up to 5 random films the the provided genre
            val filmsWithCategory = database.getRandomFilmsWithGenre(interest).shuffled().map {
                FilmFeedItem(it.name, it.imageUrls[0], it.id)
            }
            feed.add(FilmFeedResponse(interest, filmsWithCategory))
        }

        var films = database.getAllFilms()

        films = films.shuffled()

        val randomFeedItems = database.getRandomFilms().map {
            FilmFeedItem(it.name, it.imageUrls[0], it.id)
        }
        feed.add(FilmFeedResponse("Може да ти хареса", randomFeedItems))

        films = films.sortedByDescending { it.likedBy.size }
        films = if (films.size < 10) {
            films.subList(0, films.size)
        } else {
            films.subList(0, 10)
        }

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
    suspend fun searchFilmByTitle(query: String): List<FilmFeedItem> {
        return database.searchForFilms(query).map {
            FilmFeedItem(it.name, it.imageUrls[0], it.id)
        }
    }

    /**
     * @return True if the film is in user's list else false
     */
    suspend fun isFilmInUserList(filmId: String, email: String): Boolean {
        return database.isFilmAddedToList(filmId, email)
    }

    /**
     * Gets a list of all films
     * @return List of all films grouped by their main genre
     */
    suspend fun getAllFilms(): List<FilmFeedResponse> {
        val allFilms = database.getAllFilms()
        val groupedByGenres =
            allFilms.stream().collect(Collectors.groupingBy { film -> film.genres.first() })
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
        return database.getAllFilms().stream().collect(
            Collectors.groupingBy { film -> film.genres.first() }
        ).keys.sorted()
    }
}
