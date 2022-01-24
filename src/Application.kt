package eu.bbsapps.forgottenfilmsapi

import eu.bbsapps.forgottenfilmsapi.data.DataAccessObject
import eu.bbsapps.forgottenfilmsapi.data.KmongoDatabase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val database: DataAccessObject = KmongoDatabase()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }
    install(Routing) {
        route("/") {
            get {
                call.respond(HttpStatusCode.OK, "Server is up!")
            }
        }
    }
}

/**
 * Configures BasicAuth with email and password
 */
private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Movies Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (database.checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

