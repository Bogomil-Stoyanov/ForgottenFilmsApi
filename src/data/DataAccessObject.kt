package eu.bbsapps.forgottenfilmsapi.data

import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair

/**
 * This interface specifies how the system will communicate with the database. This makes development and testing
 * easier as it provides an abstract interface to some type of database
 */
interface DataAccessObject {
    /**
     * Registers a user
     * @return true if the registration was successful
     */
    suspend fun registerUser(user: User): Boolean

    /**
     * Check if a user with the same email already exists
     * @return true if a user with  the same email already exists
     */
    suspend fun checkIfUserExists(email: String): Boolean

    /**
     * Checks if the passwordToCheck matches the saved password of a user with an email
     * @return true if the passwordToCheck matches the saved password of a user with an email
     */
    suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean

    /**
     * Changes the nickname of a user with an email
     * @return true if the change was successful
     */
    suspend fun changeUserNickname(email: String, newNickname: String): Boolean

    /**
     * Adds a genre to a user with email
     * @return true if the addition was successful
     */
    suspend fun addUserGenre(email: String, genre: String): Boolean

    /**
     * Deletes all genres of a user with email
     */
    suspend fun deleteAllUserGenres(email: String)

    /**
     * Gets all genres of a user with email
     * @return a list of genres
     */
    suspend fun getUserGenres(email: String): List<String>

    /**
     * Adds a filmId to the list of a user with email
     * @return true if the addition was successful
     */
    suspend fun addFilmToUserList(email: String, filmId: String): Boolean

    /**
     * Removes a filmId to the list of a user with email
     * @return true if the removal was successful
     */
    suspend fun removeFilmFromUserList(email: String, filmId: String): Boolean

    /**
     * Gets a list of film ids of a user with email
     * @return a list of film ids
     */
    suspend fun getUserFilmIdsList(email: String): List<String>

    /**
     * Adds a film to the database
     * @return true if the addition was successful
     */
    suspend fun addMovie(film: Movie): Boolean

    /**
     * Deletes a film with id
     * @return true if the deletion was successful
     */
    suspend fun deleteMovieWithId(id: String): Boolean

    /**
     * Gets a film with id
     */
    suspend fun getMovieWithId(id: String): Movie

    /**
     * Checks if a film is likde by a user with id
     * @return true if the film is liked by the user else false
     */
    suspend fun isMovieLikedBy(filmId: String, userId: String): Boolean

    /**
     * Checks if a film is disliked by a user with email
     * @return true if the film is disliked by the user else false
     */
    suspend fun isMovieDislikedBy(filmId: String, userId: String): Boolean

    /**
     * Gets the user with an email
     * @return User who has the same email
     */
    suspend fun getUserIdByEmail(email: String): String

    /**
     * Adds a like to a film
     * @return true if the addition is successful
     */
    suspend fun addLikeToMovie(filmId: String, userId: String): Boolean

    /**
     * Removes a like from a film
     * @return true if the removal is successful
     */
    suspend fun removeLikeFromMovie(filmId: String, userId: String): Boolean

    /**
     * Adds a dislike to a film
     * @return true if the addition is successful
     */
    suspend fun addDislikeToMovie(filmId: String, userId: String): Boolean

    /**
     * Removes a like from a film
     * @return true if the removal is successful
     */
    suspend fun removeDislikeFromMovie(filmId: String, userId: String): Boolean

    /**
     * Gets the like count of a film with id
     */
    suspend fun getLikeCountForMovie(filmId: String): Int

    /**
     * Gets the dislike count of a film with id
     */
    suspend fun getDislikeCountForMovie(filmId: String): Int

    /**
     * Gets a list of all films that have the genre
     * @return a list of films with the given genre
     */
    suspend fun getMoviesWithGenre(genre: String): List<Movie>

    /**
     * Gets all films from the database
     */
    suspend fun getAllMovies(): List<Movie>

    /**
     * Gets all films that have a title that matches the query
     */
    suspend fun searchForMovies(query: String): List<Movie>

    /**
     * Checks if a film is added to a user's list with email
     * @return true if the film is added in the list
     */
    suspend fun isMovieAddedToList(filmId: String, email: String): Boolean

    /**
     * Gets the nickname of a user with the given email
     */
    suspend fun getNickname(email: String): String

    /**
     * Gets a random list of films
     */
    suspend fun getRandomMovies(): List<Movie>

    /**
     * Gets a random list of films with a genre
     */
    suspend fun getRandomMoviesWithGenre(genre: String): List<Movie>

    /**
     * Deletes a film with a name
     * @return true if the removal was successful
     */
    suspend fun deleteMovieWithName(name: String): Boolean

    /**
     * Gets the id of a film with name
     */
    suspend fun getMovieIdWithName(name: String): String

    /**
     * Deletes a user with email
     */
    suspend fun deleteUserWithEmail(email: String): Boolean

    /**
     * Adds watch time to a user
     * @return true if the addition was successful
     */
    suspend fun addWatchTime(email: String, watchTimePair: GenreWatchTimePair): Boolean

    /**
     * Gets the watch time of a user with email
     */
    suspend fun getWatchTime(email: String): List<GenreWatchTimePair>

    /**
     * Gets the total count of films
     */
    suspend fun getTotalMovieCount(): Int

    /**
     * Gets the total count of users
     */
    suspend fun getTotalUserCount(): Int

    /**
     * Gets the total watch time stats of all users
     */
    suspend fun getTotalWatchStats(): List<List<GenreWatchTimePair>>

    /**
     * Gets all users
     */
    suspend fun getAllUsers(): List<User>
}