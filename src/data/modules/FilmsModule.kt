package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem
import eu.bbsapps.forgottenfilmsapi.data.controllers.FilmsController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.responses.FilmFeedResponse
import eu.bbsapps.forgottenfilmsapi.data.responses.FilmResponse
import eu.bbsapps.forgottenfilmsapi.database
import io.ktor.http.*

object FilmsModule {

    /**
     * If the user with email has not liked to video before - a like is added,
     * else if the video has already been liked - the like is removed,
     * else if the video is disliked - the dislike is removed and a like is added
     * @return 0 - like removed else 1 - like added
     */
    suspend fun filmLiked(email: String, filmId: String): GenericResponse<Int> {

        val userId = database.getUserIdByEmail(email)

        //remove like if video is already liked
        if (FilmsController.isFilmLikedBy(filmId, userId)) {
            FilmsController.removeLikeFromFilm(filmId, userId)
            return GenericResponse(HttpStatusCode.OK, 0)
        }

        // remove dislike if video is disliked and add a like
        if (FilmsController.isFilmDislikedBy(filmId, userId)) {
            FilmsController.removeDislikeFromFilm(filmId, userId)
            FilmsController.addLikeToFilm(filmId, userId)
            return GenericResponse(HttpStatusCode.OK, 1)
        }

        FilmsController.addLikeToFilm(filmId, userId)
        return GenericResponse(HttpStatusCode.OK, 1)
    }

    /**
     * If the user with email has not disliked to video before - a dislike is added,
     * else if the video has already been disliked - the dislike is removed,
     * else if the video is liked - the like is removed and a dislike is added
     * @return 0 - dislike removed else -1 - dislike added
     */
    suspend fun filmDisliked(email: String, filmId: String): GenericResponse<Int> {
        val userId = database.getUserIdByEmail(email)

        //remove dislike if video is already disliked
        if (FilmsController.isFilmDislikedBy(filmId, userId)) {
            FilmsController.removeDislikeFromFilm(filmId, userId)
            return GenericResponse(HttpStatusCode.OK, 0)
        }

        // remove like if video is liked and add a dislike
        if (FilmsController.isFilmLikedBy(filmId, userId)) {
            FilmsController.removeLikeFromFilm(filmId, userId)
            FilmsController.addDislikeToFilm(filmId, userId)
            return GenericResponse(HttpStatusCode.OK, -1)

        }

        FilmsController.addDislikeToFilm(filmId, userId)
        return GenericResponse(HttpStatusCode.OK, -1)
    }

    /**
     * Returns the like count of a film with id
     */
    suspend fun getLikeCountForFilm(filmId: String): GenericResponse<Int> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.getLikeCountForFilm(filmId))
    }

    /**
     * Returns the dislike count of a film with id
     */
    suspend fun getDislikeCountForFilm(filmId: String): GenericResponse<Int> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.getDislikeCountForFilm(filmId))
    }

    /**
     * Gets the film for a user, for example if the user has liked the films and all other data
     */
    suspend fun getFilmForUser(email: String, filmId: String): GenericResponse<FilmResponse> {
        val userId = database.getUserIdByEmail(email)
        return GenericResponse(HttpStatusCode.OK, FilmsController.getFilmForUser(filmId, userId))
    }

    /**
     * Gets the feed of a user with email
     */
    suspend fun getUserFeed(email: String): GenericResponse<List<FilmFeedResponse>> {
        val userId = database.getUserIdByEmail(email)
        return GenericResponse(HttpStatusCode.OK, FilmsController.getUserFeed(userId))
    }

    /**
     * Returns a list of films with matching title
     */
    suspend fun searchFilmByTitle(filmTitle: String): GenericResponse<List<FilmFeedItem>> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.searchFilmByTitle(filmTitle))
    }

    /**
     * Returns if the film is in user's list
     */
    suspend fun isFilmInUserList(email: String, filmId: String): GenericResponse<Boolean> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.isFilmInUserList(filmId, email))
    }

    /**
     * Gets all films
     */
    suspend fun getAllFilms(): GenericResponse<List<FilmFeedResponse>> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.getAllFilms())
    }

    /**
     * Gets a list of all genres
     */
    suspend fun getAllGenres(): GenericResponse<List<String>> {
        return GenericResponse(HttpStatusCode.OK, FilmsController.getAllGenres())
    }
}