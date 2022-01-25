package eu.bbsapps.forgottenfilmsapi.routes

import data.requests.user.management.GenreWatchTimeRequest
import data.requests.user.management.UserGenresRequest
import eu.bbsapps.data.*
import eu.bbsapps.forgottenfilmsapi.data.operations.AccountManagementOperations
import eu.bbsapps.forgottenfilmsapi.data.responses.GenreWatchTimePair
import eu.bbsapps.forgottenfilmsapi.data.responses.SimpleResponse
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
                val newNickname = call.request.queryParameters["newNickname"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementOperations.changeNickname(email, newNickname)) {
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
                AccountManagementOperations.addUserGenres(email, request.genres)
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

                AccountManagementOperations.deleteAllUserGenres(email)
                AccountManagementOperations.addUserGenres(email, request.genres)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    route("/v1/genres") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementOperations.getUserGenres(email))
            }
        }
    }

    route("/v1/filmList") {
        authenticate {
            post {
                val movieId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementOperations.addFilmToUserList(email, movieId)) {
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
                val movieId = call.request.queryParameters["id"] ?: ""

                val email = call.principal<UserIdPrincipal>()!!.name

                if (AccountManagementOperations.removeFilmFromUserList(email, movieId)) {
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
                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementOperations.getUserFilmList(email))
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
                if (AccountManagementOperations.addWatchTime(email, pair)) {
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
                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, AccountManagementOperations.getWatchTime(email))
            }

        }
    }

    route("/v1/nickname") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                call.respond(HttpStatusCode.OK, SimpleResponse(true, AccountManagementOperations.getNickname(email)))
            }
        }
    }
}