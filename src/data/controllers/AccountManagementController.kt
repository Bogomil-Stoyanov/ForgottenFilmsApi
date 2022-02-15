package eu.bbsapps.forgottenfilmsapi.data.controllers

import eu.bbsapps.forgottenfilmsapi.data.collections.Film
import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.database

object AccountManagementController {

    /**
     * Inserts a new nickname to a user with email
     * @return If the insertion was successful
     */
    suspend fun changeNickname(email: String, newNickname: String): Boolean {
        return database.changeUserNickname(email, newNickname)
    }

    /**
     * Adds new genres to user's favourite genres list with email
     */
    suspend fun addUserGenres(email: String, genres: List<String>) {
        genres.forEach { genre ->
            database.addUserGenre(email, genre)
        }
    }

    /**
     * Removes all favourite genres of user with email
     */
    suspend fun deleteAllUserGenres(email: String) {
        database.deleteAllUserGenres(email)
    }

    /**
     * Gets favourite genres of user with userId
     */
    suspend fun getUserGenres(email: String): List<String> {
        return database.getUserGenres(email)
    }

    /**
     * Adds a film to user's film list with email
     */
    suspend fun addFilmToUserList(email: String, filmId: String): Boolean {
        return database.addFilmToUserList(email, filmId)
    }

    /**
     * Removes a film from user's film list with email
     */
    suspend fun removeFilmFromUserList(email: String, filmId: String): Boolean {
        return database.removeFilmFromUserList(email, filmId)
    }

    /**
     * Gets the user's film list with email
     */
    suspend fun getUserFilmList(email: String): List<FilmFeedItem> {
        val films = ArrayList<Film>()

        for (id in database.getUserFilmIdsList(email)) {
            films.add(database.getFilmWithId(id))
        }

        var userListFilmItems = emptyList<FilmFeedItem>()
        if (films.isNotEmpty()) {
            userListFilmItems = films.map {
                FilmFeedItem(it.name, it.imageUrls[0], it.id)
            }
        }

        return userListFilmItems
    }

    /**
     * Adds watch time to a user
     */
    suspend fun addWatchTime(email: String, genreWatchTimePair: GenreWatchTimePair): Boolean {
        return database.addWatchTime(email, genreWatchTimePair)
    }

    /**
     * Gets the watch time sorted by genres and also the total watch time in SECONDS
     */
    suspend fun getWatchTime(email: String): List<GenreWatchTimePair> {
        val watchTimePairs = database.getWatchTime(email).toMutableList()
        var totalWatchTime = 0
        for (watchTimePair in watchTimePairs) {
            totalWatchTime += watchTimePair.totalWatchTimeInSeconds
        }
        watchTimePairs.sortBy { pair -> pair.genre }
        watchTimePairs.add(0, GenreWatchTimePair("Общо", totalWatchTime))
        return watchTimePairs
    }

    /**
     * Gets the nickname of a user with email
     */
    suspend fun getNickname(email: String): String {
        return database.getNickname(email)
    }

    suspend fun setNewPassword(email: String, password: String) {
        database.updateUserPassword(email, password)
    }

    suspend fun userExists(email: String): Boolean {
        return database.checkIfUserExists(email)
    }
}