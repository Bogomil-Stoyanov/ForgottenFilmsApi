package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.modules.LoginModule
import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.security.LOGIN_API_KEY
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

            if (request.apiKey != LOGIN_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }

            val response = LoginModule.login(request)
            call.respond(response.statusCode, response.data)
        }
    }

}