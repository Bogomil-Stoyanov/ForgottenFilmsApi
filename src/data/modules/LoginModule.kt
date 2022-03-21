package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.controllers.LoginController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import eu.bbsapps.forgottenfilmsapi.localizedResponses
import io.ktor.http.*

object LoginModule {

    /**
     * Attempts login
     * @return SimpleResponse with a message depending if the login attempt was successfull
     */
    suspend fun login(request: LoginAccountRequest): GenericResponse<SimpleResponse> {
        val isPasswordCorrect = LoginController.isPasswordCorrect(request.email, request.password)

        return if (isPasswordCorrect) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, localizedResponses.getLocalisedValue("logged_in")))
        } else {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(false, localizedResponses.getLocalisedValue("wrong_credentials")))
        }
    }
}