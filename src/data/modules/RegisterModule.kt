package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.controllers.RegisterController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import eu.bbsapps.forgottenfilmsapi.localizedResponses
import io.ktor.http.*

object RegisterModule {

    /**
     * Validates account information and then registers a user
     * @return SimpleResponse depending if the registering was successful
     */
    suspend fun register(request: CreateAccountRequest): GenericResponse<SimpleResponse> {
        if (!RegisterController.isEmailValid(request.email)) {
            return GenericResponse(HttpStatusCode.UnprocessableEntity, SimpleResponse(
                false,
                localizedResponses.getLocalisedValue("invalid_email")))
        }

        if (RegisterController.userExists(request.email)) {
            return GenericResponse(
                HttpStatusCode.Conflict,
                SimpleResponse(false, localizedResponses.getLocalisedValue("user_already_exists"))
            )
        }

        if (!RegisterController.isPasswordValid(request.password)) {
            println("REQ: ${localizedResponses.getLocalisedValue("password_requirements")}")
            return GenericResponse(
                HttpStatusCode.UnprocessableEntity,
                SimpleResponse(
                    false,
                    localizedResponses.getLocalisedValue("password_requirements")
                )
            )
        }

        if (!RegisterController.isNicknameValid(request.nickname)) {
            return GenericResponse(HttpStatusCode.OK, SimpleResponse(
                false,
                localizedResponses.getLocalisedValue("name_cannot_be_empty")))
        }

        val hashedPassword = RegisterController.hashPassword(request.password)

        val userSuccessfullyRegistered = RegisterController.registerUser(
            User(
                request.email,
                hashedPassword,
                request.nickname,
                request.genres,
                emptyList(),
                emptyList()
            )
        )

        return if (userSuccessfullyRegistered) {
            GenericResponse(HttpStatusCode.OK, SimpleResponse(
                true,  localizedResponses.getLocalisedValue("account_created_successfully")))
        } else {
            GenericResponse(
                HttpStatusCode.InternalServerError,
                SimpleResponse(false, localizedResponses.getLocalisedValue("unknown_error"))
            )
        }
    }
}