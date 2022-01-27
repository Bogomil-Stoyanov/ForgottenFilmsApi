package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.controllers.AdminController
import eu.bbsapps.forgottenfilmsapi.data.requests.admin.AddMoviesRequest
import eu.bbsapps.forgottenfilmsapi.security.ACCOUNT_MANAGEMENT_API_KEY
import eu.bbsapps.forgottenfilmsapi.security.ADMIN_API_KEY
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val ADMIN_EMAIL = "admin@goldenfilms.gf"
val adminEmails = listOf("admin@goldenfilms.gf")

fun Route.movieAdminRoute() {

    route("/v1/movies") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddMoviesRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                // admin@goldenfilms.gf GoldenFilms_123
                //admin@forgottenfilms.ff ForgottenFilms_123
                if (email == ADMIN_EMAIL && request.apiKey == ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Created, AdminController.insertMovies(request.movies))
                    return@post
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
            }
        }
    }

    route("/v1/movies") {
        authenticate {
            delete {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val movieName = call.request.queryParameters["movieName"] ?: ""

                val email = call.principal<UserIdPrincipal>()!!.name

                if (email == ADMIN_EMAIL) {
                    AdminController.deleteMovieWithName(movieName)
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
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val userEmail = call.request.queryParameters["email"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (email == ADMIN_EMAIL) {
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

    route("/v1/moviesCount") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                if (email == ADMIN_EMAIL) {
                    call.respond(HttpStatusCode.OK, AdminController.getMovieCount())
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
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                if (email == ADMIN_EMAIL) {
                    call.respond(HttpStatusCode.OK, AdminController.getUserCount())
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
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                if (email == ADMIN_EMAIL) {
                    call.respond(HttpStatusCode.OK, AdminController.getAdminStatistics())
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
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY || apiKey != ADMIN_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, email == ADMIN_EMAIL)
            }

        }
    }

    route("/v1/users") {
        authenticate {
            get {

                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                if (email == ADMIN_EMAIL) {
                    call.respond(HttpStatusCode.OK, AdminController.getAllUsers())
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
            }
        }
    }
}