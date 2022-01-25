package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.data.operations.RegisterOperations
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

//SUPER SECRET API KEY: f8c4a066-5837-416e-886b-ca0b2beac190

fun Route.registerRoute() {
    route("/v1/register") {
        post {
            val request = try {
                call.receive<CreateAccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (request.apiKey != "f8c4a066-5837-416e-886b-ca0b2beac190") {
                call.respond(HttpStatusCode.Forbidden)
            }

            if (!RegisterOperations.isEmailValid(request.email)) {
                call.respond(HttpStatusCode.UnprocessableEntity, SimpleResponse(false, "Невалиден имейл"))
                return@post
            }

            if (RegisterOperations.userExists(request.email)) {
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(false, "Потребител със същия имейл вече съществува")
                )
                return@post
            }

            if (!RegisterOperations.isPasswordValid(request.password)) {
                call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    SimpleResponse(
                        false,
                        "Паролата трябва да бъде най-малко 8 знака и трябва да съдържа малки и главни букви, цифри и специални символи (@#\$%^&amp;+=_)"
                    )
                )
                return@post
            }

            if (!RegisterOperations.isNicknameValid(request.nickname)) {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "Името не може да бъде празно"))
                return@post
            }

            val hashedPassword = RegisterOperations.hashPassword(request.password)

            val userSuccessfullyRegistered = RegisterOperations.registerUser(
                User(
                    request.email,
                    hashedPassword,
                    request.nickname,
                    request.genres,
                    emptyList(),
                    emptyList()
                )
            )

            if (userSuccessfullyRegistered) {
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Успешно създаден профил"))
                return@post
            } else {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    SimpleResponse(false, "Възникна неочаквана грешка")
                )
                return@post
            }
        }
    }
}