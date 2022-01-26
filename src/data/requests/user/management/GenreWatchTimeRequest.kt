package eu.bbsapps.forgottenfilmsapi.data.requests.user.management

data class GenreWatchTimeRequest(
    val genre: String,
    val additionalWatchTimeInSeconds: Int
)