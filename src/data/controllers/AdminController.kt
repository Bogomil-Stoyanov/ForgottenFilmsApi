package eu.bbsapps.forgottenfilmsapi.data.controllers

import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.UserResponse
import eu.bbsapps.forgottenfilmsapi.database
import java.util.stream.Collectors

object AdminController {

    /**
     * Inserts the given list of movies in the database
     * @return a list of inserted movies id as String
     */
    suspend fun insertMovies(movies: List<Movie>): List<String> {
        val movieIds = mutableListOf<String>()
        movies.forEach { movie ->
            movieIds.add(insertMovie(movie))
        }
        return movieIds
    }

    /**
     * Inserts a signle movie in the database
     * @return inserted movie id as String
     */
    private suspend fun insertMovie(movie: Movie): String {
        database.addMovie(movie)
        return database.getMovieIdWithName(movie.name)
    }

    /**
     * Deletes a movie from the database with a given name
     */
    suspend fun deleteMovieWithName(movieName: String) {
        database.deleteMovieWithName(movieName)
    }

    /**
     * Deletes a user from the database with a given email
     */
    suspend fun deleteUserWithEmail(email: String) {
        database.deleteUserWithEmail(email)
    }

    /**
     * Get the count of all users
     * @return total user count
     */
    suspend fun getUserCount(): Int {
        return database.getTotalUserCount()
    }

    /**
     * Get the count of all movies
     * @return total user count
     */
    suspend fun getMovieCount(): Int {
        return database.getTotalMovieCount()
    }

    /**
     * Generates the statistics for the admin. Provides total watch time in SECONDS by genres, sorted alphabetically.
     * Also gives total watch time in seconds
     */
    suspend fun getAdminStatistics(): List<GenreWatchTimePair> {
        val genres = database.getAllMovies().stream().collect(
            Collectors.groupingBy { movie -> movie.genres.first() }
        ).keys.sorted()

        val usersWatchStats = database.getTotalWatchStats()

        val watchTimePairs = mutableListOf<GenreWatchTimePair>()

        genres.forEach { genre ->
            watchTimePairs.add(GenreWatchTimePair(genre, 0))
        }

        var totalWatchTime = 0

        for (userWatchTime in usersWatchStats) {
            for (watchTimePair in userWatchTime as List<org.bson.Document>) {
                //extracts the genre and the totalWatchTime in seconds from the Document
                val genre = watchTimePair.get("genre")
                val totalWatchTimeInSeconds: Int =
                    Integer.parseInt((watchTimePair.get("totalWatchTimeInSeconds").toString()))
                totalWatchTime += totalWatchTimeInSeconds
                val genreIndex = genres.indexOf(genre)
                if (genreIndex != -1) {
                    val genreWatchTimePair = watchTimePairs.removeAt(genreIndex)
                    val newPair = GenreWatchTimePair(
                        genreWatchTimePair.genre,
                        genreWatchTimePair.totalWatchTimeInSeconds + totalWatchTimeInSeconds
                    )
                    watchTimePairs.add(genreIndex, newPair)
                }
            }

        }
        watchTimePairs.add(0, GenreWatchTimePair("Общо", totalWatchTime))
        return watchTimePairs
    }

    /**
     * Gets a list of all users
     * @return a list of all users in the form of UserResponse
     */
    suspend fun getAllUsers(): List<UserResponse> {
        return database.getAllUsers().map { user -> UserResponse(user.email, user.nickname) }
    }
}