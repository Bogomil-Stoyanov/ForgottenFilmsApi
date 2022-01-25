package eu.bbsapps.forgottenfilmsapi.data.requests.user.login

data class LoginAccountRequest(
    val email: String,
    val password: String,
)