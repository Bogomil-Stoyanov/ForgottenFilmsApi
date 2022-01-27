package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.collections.Movie
import eu.bbsapps.forgottenfilmsapi.data.controllers.AdminController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.UserResponse
import eu.bbsapps.forgottenfilmsapi.routes.adminEmails
import io.ktor.http.*

object AdminModule {

    /**
     * Adds new movies to the database and returns its ids
     */
    suspend fun insertMovies(films: List<Movie>): GenericResponse<List<String>> {
        return GenericResponse(HttpStatusCode.Created, AdminController.insertMovies(films))
    }

    /**
     * Deletes a movie from the database with a given name
     */
    suspend fun deleteMovieWithName(filmName: String) {
        AdminController.deleteMovieWithName(filmName)
    }

    /**
     * Gets the count of movies
     */
    suspend fun getMovieCount(): GenericResponse<Int> {
        return GenericResponse(HttpStatusCode.OK, AdminController.getMovieCount())
    }

    /**
     * Gets the count of users
     */
    suspend fun getUserCount(): GenericResponse<Int> {
        return GenericResponse(HttpStatusCode.OK, AdminController.getUserCount())
    }

    /**
     * Gets the admin stats by calling AdminController.getAdminStats()
     */
    suspend fun getAdminStats(): GenericResponse<List<GenreWatchTimePair>> {
        return GenericResponse(HttpStatusCode.OK, AdminController.getAdminStatistics())
    }

    /**
     * Checks if email is admin email
     */
    fun isAdmin(email: String): GenericResponse<Boolean> {
        return GenericResponse(HttpStatusCode.OK, adminEmails.contains(email))
    }

    /**
     * Gets all users and returns their emails and nicknames
     */
    suspend fun getAllUsers(): GenericResponse<List<UserResponse>> {
        return GenericResponse(HttpStatusCode.OK, AdminController.getAllUsers())
    }
}