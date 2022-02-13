package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.collections.FilmFeedItem
import eu.bbsapps.forgottenfilmsapi.data.controllers.AccountManagementController
import eu.bbsapps.forgottenfilmsapi.data.controllers.RegisterController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.requests.user.management.GenreWatchTimeRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import io.ktor.http.*
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail
import kotlin.random.Random

object AccountManagementModule {


    /**
     * Updates user's nickname with an email
     */
    suspend fun updateNickname(newNickname: String, email: String): GenericResponse<SimpleResponse> {
        return if (AccountManagementController.changeNickname(email, newNickname)) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Успешно смени името си"))
        } else {
            GenericResponse(
                HttpStatusCode.InternalServerError,
                SimpleResponse(false, "Възникна неочаквана грешка")
            )
        }
    }

    /**
     * Calls AccountManagementController.addUserGenres() with the provided email and genres
     */
    suspend fun addGenresToUser(genres: List<String>, email: String) {
        AccountManagementController.addUserGenres(email, genres)
    }

    /**
     * Clears all user genres with email and then adds new genres
     */
    suspend fun updateUserGenres(genres: List<String>, email: String) {
        AccountManagementController.deleteAllUserGenres(email)
        AccountManagementController.addUserGenres(email, genres)
    }

    /**
     * Gets all user's genres with email
     */
    suspend fun getUserGenres(email: String): GenericResponse<List<String>> {
        return GenericResponse(HttpStatusCode.OK, AccountManagementController.getUserGenres(email))
    }

    /**
     * Adds a filmId to a user's film list with email
     */
    suspend fun addFilmToUserList(filmId: String, email: String): GenericResponse<SimpleResponse> {
        return if (AccountManagementController.addFilmToUserList(email, filmId)) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Филмът е добавен към списъка ти"))
        } else {
            GenericResponse(
                HttpStatusCode.InternalServerError,
                SimpleResponse(false, "Възникна неочаквана грешка")
            )
        }
    }

    /**
     * Removes a filmId from user's list with email
     */
    suspend fun removeFilmFromUserList(filmId: String, email: String): GenericResponse<SimpleResponse> {
        return if (AccountManagementController.removeFilmFromUserList(email, filmId)) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Филмът е премахнат от списъка ти"))
        } else {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(false, "Филмът не е в списъка ти"))
        }
    }

    /**
     * Gets the film list of a user with email
     */
    suspend fun getUserFilmList(email: String): GenericResponse<List<FilmFeedItem>> {
        return GenericResponse(HttpStatusCode.OK, AccountManagementController.getUserFilmList(email))
    }

    /**
     * Adds a watch time to a user with email
     */
    suspend fun addUserWatchTime(
        genreWatchTimeRequest: GenreWatchTimeRequest,
        email: String
    ): GenericResponse<SimpleResponse> {
        val pair = GenreWatchTimePair(genreWatchTimeRequest.genre, genreWatchTimeRequest.additionalWatchTimeInSeconds)
        return if (AccountManagementController.addWatchTime(email, pair)) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Успешно обновено време за гледане"))
        } else {
            GenericResponse(
                HttpStatusCode.InternalServerError,
                SimpleResponse(false, "Възникна неочаквана грешка")
            )
        }
    }

    /**
     * Gets the watch time of a user with email
     */
    suspend fun getWatchTime(email: String): GenericResponse<List<GenreWatchTimePair>> {
        return GenericResponse(HttpStatusCode.OK, AccountManagementController.getWatchTime(email))
    }


    /**
     * Gets the nickname of a user with email
     */
    suspend fun getNickname(email: String): GenericResponse<SimpleResponse> {
        return GenericResponse(HttpStatusCode.OK, SimpleResponse(true, AccountManagementController.getNickname(email)))
    }

    /**
     * Checks if user with the email exists, generates a new password and sends and email to the user
     */
    suspend fun forgottenPassword(email: String): GenericResponse<SimpleResponse> {
        if (!AccountManagementController.userExists(email)) {
            return GenericResponse(HttpStatusCode.OK, SimpleResponse(false, "Потребител с този имейл не съществува"))
        }

        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val newPassword = (1..8)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val hashedPassword = RegisterController.hashPassword(newPassword)
        AccountManagementController.setNewPassword(email, hashedPassword)

        val responseEmail = SimpleEmail()
        responseEmail.hostName = "smtp.googlemail.com"
        responseEmail.setSmtpPort(465)
        responseEmail.setAuthenticator(DefaultAuthenticator("help.bbsapps@gmail.com", "androidsupport"))
        responseEmail.isSSLOnConnect = true
        responseEmail.setFrom("help.bbsapps@gmail.com")
        responseEmail.subject = "Възстановяване на парола - Забравените филми"
        responseEmail.setMsg(
            "Здравейте, Влезте в системата на Забравените филми със следната парола: $newPassword. " +
                    "След това отидете в раздел 'Още' и сменете паролата"
        )
        responseEmail.addTo(email)
        responseEmail.send()

        return GenericResponse(
            HttpStatusCode.OK, SimpleResponse(
                true,
                "Изпратен е имейл на $email, съдържащ инструкции са възстановяване на акаунта"
            )
        )
    }

    /**
     * Check if the new password is valid and saves it
     */
    suspend fun changePassword(email: String, newPassword: String): GenericResponse<SimpleResponse> {
        if (!AccountManagementController.userExists(email)) {
            return GenericResponse(HttpStatusCode.OK, SimpleResponse(false, "Потребител с този имейл не съществува"))
        }

        if (!RegisterController.isPasswordValid(newPassword)) {

            return GenericResponse(
                HttpStatusCode.UnprocessableEntity,
                SimpleResponse(
                    false,
                    "Паролата трябва да бъде най-малко 8 знака и трябва да съдържа малки и главни букви, цифри и специални символи (@#\$%^&amp;+=_)"
                )
            )
        }

        val hashedPassword = RegisterController.hashPassword(newPassword)
        AccountManagementController.setNewPassword(email, hashedPassword)
        return GenericResponse(
            HttpStatusCode.OK,
            SimpleResponse(true, "Паролата е сменена, моля излезте и влезте в систамата")
        )
    }
}