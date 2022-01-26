package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.controllers.AccountManagementController
import eu.bbsapps.forgottenfilmsapi.data.requests.user.management.GenreWatchTimeRequest
import eu.bbsapps.forgottenfilmsapi.data.requests.user.management.UserGenresRequest
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
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
                }

                val newNickname = call.request.queryParameters["newNickname"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementController.changeNickname(email, newNickname)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Успешно смени името си"))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        SimpleResponse(false, "Възникна неочаквана грешка")
                    )
                }
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
                val email = call.principal<UserIdPrincipal>()!!.name
                AccountManagementController.addUserGenres(email, request.genres)
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
                val email = call.principal<UserIdPrincipal>()!!.name

                AccountManagementController.deleteAllUserGenres(email)
                AccountManagementController.addUserGenres(email, request.genres)
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
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementController.getUserGenres(email))
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            post {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val movieId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementController.addFilmToUserList(email, movieId)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Филмът е добавен към списъка ти"))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        SimpleResponse(false, "Възникна неочаквана грешка")
                    )
                }
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            delete {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val movieId = call.request.queryParameters["id"] ?: ""

                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementController.removeFilmFromUserList(email, movieId)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Филмът е премахнат от списъка ти"))
                } else {
                    call.respond(HttpStatusCode.OK, SimpleResponse(false, "Филмът не е в списъка ти"))
                }
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementController.getUserFilmList(email))
            }
        }
    }

    route("/v1/watchTime") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<GenreWatchTimeRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                val pair = GenreWatchTimePair(request.genre, request.additionalWatchTimeInSeconds)
                if (AccountManagementController.addWatchTime(email, pair)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Успешно обновено време за гледане"))
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        SimpleResponse(false, "Възникна неочаквана грешка")
                    )
                }
            }
        }
    }

    route("/v1/watchTime") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementController.getWatchTime(email))
            }

        }
    }

    route("/v1/nickname") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != ACCOUNT_MANAGEMENT_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                }

                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, SimpleResponse(true, AccountManagementController.getNickname(email)))
            }
        }
    }
}