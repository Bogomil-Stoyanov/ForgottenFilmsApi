package eu.bbsapps.forgottenfilmsapi.data.requests.admin

data class DeleteFilmRequest(
    val names: List<String>,
    val apiKey: String
)
