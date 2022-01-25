package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.operations.LoginOperations
import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginRoute() {
    route("/v1/login") {
        post {
            val request = try {
                call.receive<LoginAccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isPasswordCorrect = LoginOperations.isPasswordCorrect(request.email, request.password)

            if (isPasswordCorrect) {
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Влязохте в системата"))
            } else {
                call.respond(HttpStatusCode.OK, SimpleResponse(false, "Грешен имейл или парола"))
            }
        }
    }

}