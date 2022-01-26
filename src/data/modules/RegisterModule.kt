package eu.bbsapps.forgottenfilmsapi.data.modules

import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.controllers.RegisterController
import eu.bbsapps.forgottenfilmsapi.data.modules.util.GenericResponse
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import io.ktor.http.*

object RegisterModule {

    suspend fun register(request: CreateAccountRequest): GenericResponse<SimpleResponse> {
        if (!RegisterController.isEmailValid(request.email)) {
            return GenericResponse(HttpStatusCode.UnprocessableEntity, SimpleResponse(false, "Невалиден имейл"))
        }

        if (RegisterController.userExists(request.email)) {
            return GenericResponse(
                HttpStatusCode.Conflict,
                SimpleResponse(false, "Потребител със същия имейл вече съществува")
            )
        }

        if (!RegisterController.isPasswordValid(request.password)) {
            return GenericResponse(
                HttpStatusCode.UnprocessableEntity,
                SimpleResponse(
                    false,
                    "Паролата трябва да бъде най-малко 8 знака и трябва да съдържа малки и главни букви, цифри и специални символи (@#\$%^&amp;+=_)"
                )
            )
        }

        if (!RegisterController.isNicknameValid(request.nickname)) {
            return GenericResponse(HttpStatusCode.OK, SimpleResponse(false, "Името не може да бъде празно"))
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
            GenericResponse(HttpStatusCode.OK, SimpleResponse(true, "Успешно създаден профил"))
        } else {
            GenericResponse(
                HttpStatusCode.InternalServerError,
                SimpleResponse(false, "Възникна неочаквана грешка")
            )
        }
    }
}