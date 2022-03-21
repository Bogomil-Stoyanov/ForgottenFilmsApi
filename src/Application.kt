package eu.bbsapps.forgottenfilmsapi

import eu.bbsapps.forgottenfilmsapi.data.DataAccessObject
import eu.bbsapps.forgottenfilmsapi.data.KmongoDatabase
import eu.bbsapps.forgottenfilmsapi.data.TestDatabase
import eu.bbsapps.forgottenfilmsapi.routes.*
import eu.bbsapps.forgottenfilmsapi.util.Localization
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

val productionDatabase: DataAccessObject = KmongoDatabase()
val testDatabase: DataAccessObject = TestDatabase()
var database: DataAccessObject = productionDatabase
var localizedResponses = Localization()

@Suppress("unused")
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
        registerRoute()
        loginRoute()
        accountManagementRoute()
        filmAdminRoute()
        filmsRoute()
    }
}

/**
 * Configures BasicAuth with email and password
 */
private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Forgotten Films Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (database.checkPasswordForEmail(email, password)) {
                UserIdPrincipal(email)
            } else null
        }
    }
}

