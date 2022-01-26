package data

import eu.bbsapps.forgottenfilmsapi.data.requests.user.login.LoginAccountRequest
import eu.bbsapps.forgottenfilmsapi.data.requests.user.register.CreateAccountRequest
import eu.bbsapps.forgottenfilmsapi.security.LOGIN_API_KEY
import eu.bbsapps.forgottenfilmsapi.security.REGISTER_API_KEY

val validCreateAccountRequest = CreateAccountRequest(
    email = "valid@email.com",
    password = "Pass_123",
    nickname = "Valid nickname",
    genres = listOf("Genre1", "Genre2", "Genre3"),
    apiKey = REGISTER_API_KEY
)

val validLoginRequest = LoginAccountRequest(
    email = validCreateAccountRequest.email,
    password = validCreateAccountRequest.password,
    apiKey = LOGIN_API_KEY
)
