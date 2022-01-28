package eu.bbsapps.forgottenfilmsapi.routes

import eu.bbsapps.forgottenfilmsapi.data.modules.FilmsModule
import eu.bbsapps.forgottenfilmsapi.security.MOVIES_API_KEY
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.filmsRoute() {

    route("/v1/filmLike") {
        authenticate {
            post {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
                val email = call.principal<UserIdPrincipal>()!!.name
                val filmId = call.request.queryParameters["id"] ?: ""
                val response = FilmsModule.filmLiked(email, filmId)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/filmDislike") {
        authenticate {
            post {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@post
                }
                val email = call.principal<UserIdPrincipal>()!!.name
                val filmId = call.request.queryParameters["id"] ?: ""
                val response = FilmsModule.filmDisliked(email, filmId)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/likes") {
        get {
            val apiKey = call.request.queryParameters["apiKey"] ?: ""
            if (apiKey != MOVIES_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }
            val filmId = call.request.queryParameters["id"] ?: ""
            val response = FilmsModule.getLikeCountForFilm(filmId)
            call.respond(response.statusCode, response.data)
        }
    }

    route("/v1/dislikes") {
        get {
            val apiKey = call.request.queryParameters["apiKey"] ?: ""
            if (apiKey != MOVIES_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }
            val filmId = call.request.queryParameters["id"] ?: ""
            val response = FilmsModule.getDislikeCountForFilm(filmId)
            call.respond(response.statusCode, response.data)
        }
    }

    route("/v1/film") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val filmId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name
                val response = FilmsModule.getFilmForUser(email, filmId)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/feed") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val email = call.principal<UserIdPrincipal>()!!.name
                val response = FilmsModule.getUserFeed(email)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/search") {
        get {
            val apiKey = call.request.queryParameters["apiKey"] ?: ""
            if (apiKey != MOVIES_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }
            val query = call.request.queryParameters["query"] ?: ""
            val response = FilmsModule.searchFilmByTitle(query)
            call.respond(response.statusCode, response.data)
        }
    }

    route("/v1/filmInList") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val filmId = call.request.queryParameters["id"] ?: ""
                val email = call.principal<UserIdPrincipal>()!!.name
                val response = FilmsModule.isFilmInUserList(email, filmId)
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/films") {
        authenticate {
            get {
                val apiKey = call.request.queryParameters["apiKey"] ?: ""
                if (apiKey != MOVIES_API_KEY) {
                    call.respond(HttpStatusCode.Forbidden)
                    return@get
                }
                val response = FilmsModule.getAllFilms()
                call.respond(response.statusCode, response.data)
            }
        }
    }

    route("/v1/genres") {
        get {
            val apiKey = call.request.queryParameters["apiKey"] ?: ""
            if (apiKey != MOVIES_API_KEY) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }
            val response = FilmsModule.getAllGenres()
            call.respond(response.statusCode, response.data)
        }
    }
}