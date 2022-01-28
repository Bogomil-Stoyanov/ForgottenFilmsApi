package data

import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest

val validCreateAccountRequest = CreateAccountRequest(
    email = "valid@email.com",
    password = "Pass_123",
    nickname = "Valid nickname",
    genres = listOf("Genre1", "Genre2", "Genre3")
)

val validAdminAccountRequest = CreateAccountRequest(
    email = "admin@goldenfilms.gf",
    password = "GoldenFilms_123",
    nickname = "Admin",
    genres = listOf("Genre1", "Genre2", "Genre3")
)

val validLoginRequest = LoginAccountRequest(
    email = validCreateAccountRequest.email,
    password = validCreateAccountRequest.password
)
