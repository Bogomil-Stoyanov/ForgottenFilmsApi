package eu.bbsapps.forgottenfilmsapi.data.requests.admin

data class DeleteMovieRequest(
    val names: List<String>,
    val apiKey: String
)
