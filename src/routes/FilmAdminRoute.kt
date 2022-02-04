package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.controllers.AdminController
import eu.bbsapps.forgottenfilmsapi.data.modules.AdminModule
import eu.bbsapps.forgottenfilmsapi.data.requests.admin.AddFilmsRequest
import eu.bbsapps.forgottenfilmsapi.security.ACCOUNT_MANAGEMENT_API_KEY
import eu.bbsapps.forgottenfilmsapi.security.ADMIN_API_KEY
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

val adminEmails = listOf("admin_forgotten_films@dir.bg")

// admin@goldenfilms.gf GoldenFilms_123

fun Route.filmAdminRoute() {

    route("/v1/films") {
        authenticate {
            post {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
                val request = try {
                    call.receive<AddFilmsRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                if (adminEmails.contains(email)) {
                    val response = AdminModule.insertFilms(request.films)
                    call.respond(response.statusCode, response.data)
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
            }
        }
    }

    route("/v1/films") {
        authenticate {
            delete {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }

                val filmName = call.request.queryParameters["filmName"] ?: ""

                val email = call.principal<UserIdPrincipal>()!!.name

                if (adminEmails.contains(email)) {
                    AdminModule.deleteFilmWithName(filmName)
                    call.respond(HttpStatusCode.NoContent)
                    return@delete
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }
            }
        }
    }

    route("/v1/user") {
        authenticate {
            delete {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }

                val userEmail = call.request.queryParameters["email"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (adminEmails.contains(email)) {
                    AdminController.deleteUserWithEmail(userEmail)
                    call.respond(HttpStatusCode.NoContent)
                    return@delete
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }
            }
        }
    }

    route("/v1/filmsCount") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                if (adminEmails.contains(email)) {
                    val response = AdminModule.getFilmCount()
                    call.respond(response.statusCode, response.data)
                    return@get
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
            }
        }
    }

    route("/v1/usersCount") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                if (adminEmails.contains(email)) {
                    val response = AdminModule.getUserCount()
                    call.respond(response.statusCode, response.data)
                    return@get
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
            }
        }
    }

    route("/v1/stats") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                if (adminEmails.contains(email)) {
                    val response = AdminModule.getAdminStats()
                    call.respond(response.statusCode, response.data)
                    return@get
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
            }
        }
    }

    route("/v1/isAdmin") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY && apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                val response = AdminModule.isAdmin(email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/users") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                if (adminEmails.contains(email)) {
                    val response = AdminModule.getAllUsers()
                    call.respond(response.statusCode, response.data)
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
            }
        }
    }
}