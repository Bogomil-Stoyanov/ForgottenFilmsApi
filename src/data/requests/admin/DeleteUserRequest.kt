package eu.bbsapps.forgottenfilmsapi.data.requests.admin

data class DeleteUserRequest(
    val userEmail: String,
    val apiKey: String
)
