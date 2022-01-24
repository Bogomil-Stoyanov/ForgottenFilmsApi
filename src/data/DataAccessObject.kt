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
    suspend fun getUserInterests(email: String): List<String>

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
     * Gets a list of movie ids of a user with email
     * @return a list of movie ids
     */
    suspend fun getUserFilmIdsList(email: String): List<String>

    /**
     * Adds a movie to the database
     * @return true if the addition was successful
     */
    suspend fun addMovie(movie: Movie): Boolean

    /**
     * Deletes a movie with id
     * @return true if the deletion was successful
     */
    suspend fun deleteMovieWithId(id: String): Boolean

    /**
     * Gets a movie with id
     */
    suspend fun getMovieWithId(id: String): Movie

    /**
     * Checks if a movie is likde by a user with email
     * @return true if the movie is liked by the user else false
     */
    suspend fun isMovieLikedBy(movieId: String, email: String): Boolean

    /**
     * Checks if a movie is disliked by a user with email
     * @return true if the movie is disliked by the user else false
     */
    suspend fun isMovieDislikedBy(movieId: String, userId: String): Boolean

    /**
     * Gets the user with an email
     * @return User who has the same email
     */
    suspend fun getUserIdByEmail(email: String): String

    /**
     * Adds a like to a movie
     * @return true if the addition is successful
     */
    suspend fun addLikeToMovie(movieId: String, userId: String): Boolean

    /**
     * Removes a like from a movie
     * @return true if the removal is successful
     */
    suspend fun removeLikeFromMovie(movieId: String, userId: String): Boolean

    /**
     * Adds a dislike to a movie
     * @return true if the addition is successful
     */
    suspend fun addDislikeToMovie(movieId: String, userId: String): Boolean

    /**
     * Removes a like from a movie
     * @return true if the removal is successful
     */
    suspend fun removeDislikeFromMovie(movieId: String, userId: String): Boolean

    /**
     * Gets the like count of a movie with id
     */
    suspend fun getLikeCountForMovie(movieId: String): Int

    /**
     * Gets the dislike count of a movie with id
     */
    suspend fun getDislikeCountForMovie(movieId: String): Int

    /**
     * Gets a list of all movies that have the genre
     * @return a list of movies with the given genre
     */
    suspend fun getMoviesWithGenre(genre: String): List<Movie>

    /**
     * Gets all movies from the database
     */
    suspend fun getAllMovies(): List<Movie>

    /**
     * Gets all movies that have a title that matches the query
     */
    suspend fun searchForMovies(query: String): List<Movie>

    /**
     * Checks if a movie is added to a user's list with email
     * @return true if the movie is added in the list
     */
    suspend fun isMovieAddedToList(movieId: String, email: String): Boolean

    /**
     * Gets the nickname of a user with the given email
     */
    suspend fun getNickname(email: String): String

    /**
     * Gets a random list of movies
     */
    suspend fun getRandomMovies(): List<Movie>

    /**
     * Gets a random list of movies with a genre
     */
    suspend fun getRandomMoviesWithGenre(genre: String): List<Movie>

    /**
     * Deletes a movie with a name
     * @return true if the removal was successful
     */
    suspend fun deleteMovieWithName(name: String): Boolean

    /**
     * Gets the id of a movie with name
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
     * Gets the total count of movies
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