package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.modules.AccountManagementModule
import eu.bbsapps.forgottenfilmsapi.data.requests.user.management.GenreWatchTimeRequest
import eu.bbsapps.forgottenfilmsapi.data.requests.user.management.UserGenresRequest
import eu.bbsapps.forgottenfilmsapi.security.ACCOUNT_MANAGEMENT_API_KEY
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.accountManagementRoute() {

    route("/v1/nickname") {
        authenticate {
            patch {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@patch
                }

                val newNickname = call.request.queryParameters["newNickname"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.updateNickname(newNickname, email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/genres") {
        authenticate {
            post {
                val request = try {
                    call.receive<UserGenresRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                if (request.apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                AccountManagementModule.addGenresToUser(request.genres, email)
                call.respond(HttpStatusCode.NoContent)
            }

        }
    }

    route("/v1/genres") {
        authenticate {
            patch {
                val request = try {
                    call.receive<UserGenresRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }

                if (request.apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@patch
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                AccountManagementModule.updateUserGenres(request.genres, email)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    route("/v1/genres") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                val response = AccountManagementModule.getUserGenres(email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            post {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }

                val filmId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.addFilmToUserList(filmId, email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            delete {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@delete
                }

                val filmId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.removeFilmFromUserList(filmId, email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.getUserFilmList(email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/watchTime") {
        authenticate {
            post {
                val request = try {
                    call.receive<GenreWatchTimeRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                if (request.apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.addUserWatchTime(request, email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/watchTime") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                val response = AccountManagementModule.getWatchTime(email)
                call.respond(response.statusCode, response.data)
            }

        }
    }

    route("/v1/nickname") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }

                val email = call.principal<UserIdPrincipal>()!!.name

                val response = AccountManagementModule.getNickname(email)
                call.respond(response.statusCode, response.data)
            }
        }
    }
}