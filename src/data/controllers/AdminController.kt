package eu.bbsapps.forgottenfilmsapi.data.controllers

import eu.bbsapps.forgottenfilmsapi.data.collections.Film
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.UserResponse
import eu.bbsapps.forgottenfilmsapi.database
import java.util.stream.Collectors

object AdminController {

    /**
     * Inserts the given list of films in the database
     * @return a list of inserted films id as String
     */
    suspend fun insertFilms(films: List<Film>): List<String> {
        val filmIds = mutableListOf<String>()
        films.forEach { film ->
            filmIds.add(insertFilm(film))
        }
        return filmIds
    }

    /**
     * Inserts a signle film in the database
     * @return inserted film id as String
     */
    private suspend fun insertFilm(film: Film): String {
        database.addFilm(film)
        return database.getFilmIdWithName(film.name)
    }

    /**
     * Deletes a film from the database with a given name
     */
    suspend fun deleteFilmWithName(filmName: String) {
        database.deleteFilmWithName(filmName)
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
     * Get the count of all films
     * @return total user count
     */
    suspend fun getFilmCount(): Int {
        return database.getTotalFilmCount()
    }

    /**
     * Generates the statistics for the admin. Provides total watch time in SECONDS by genres, sorted alphabetically.
     * Also gives total watch time in seconds
     */
    suspend fun getAdminStatistics(): List<GenreWatchTimePair> {
        val genres = database.getAllFilms().stream().collect(
            Collectors.groupingBy { film -> film.genres.first() }
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
        watchTimePairs.add(0, GenreWatchTimePair("????????", totalWatchTime))
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