package eu.bbsapps.forgottenfilmsapi.data.requests.user.management

data class UserGenresRequest(
    val genres: List<String>,
    val apiKey: String
)
