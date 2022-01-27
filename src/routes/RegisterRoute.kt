package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.modules.RegisterModule
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.security.REGISTER_API_KEY
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/v1/register") {
        post {
            val request = try {
                call.receive<CreateAccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            if (request.apiKey != REGISTER_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }

            val response = RegisterModule.register(request)
            call.respond(response.statusCode, response.data)
        }
    }
}